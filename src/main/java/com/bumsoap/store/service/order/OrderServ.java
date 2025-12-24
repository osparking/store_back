package com.bumsoap.store.service.order;

import com.bumsoap.store.dto.*;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.exception.OrderIdNotFoundEx;
import com.bumsoap.store.exception.UnauthorizedException;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderItemRepo;
import com.bumsoap.store.repository.OrderRepo;
import com.bumsoap.store.request.ReviewUpdateReq;
import com.bumsoap.store.request.UpdateWaybillNoReq;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.service.recipient.RecipientServI;
import com.bumsoap.store.service.soap.FeeEtcServI;
import com.bumsoap.store.util.*;
import com.trackingmore.TrackingMore;
import com.trackingmore.exception.TrackingMoreException;
import com.trackingmore.model.tracking.CreateTrackingParams;
import com.trackingmore.model.tracking.Tracking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OrderServ implements OrderServI {
    private static final Logger logger =
            LoggerFactory.getLogger(OrderServ.class);
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final SubTotaler subTotaler;
    private final RecipientServI recipientServ;
    private final AddressBasisServI addressBasisServ;
    private final FeeEtcServI feeEtcServ;

    @Autowired
    private OrderIdGenerator orderIdGenerator;

    @Autowired
    private BsParameters provider;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${trackingmore.api-key}")
    private String trackingmoreApiKey;

    @Transactional
    @Override
    public boolean updateReview(ReviewUpdateReq reqeust, Long userId) {
        Long orderId = reqeust.getId();
        var theOrder = orderRepo.findById(orderId).orElseThrow(
                () -> new IdNotFoundEx("없는 주문 ID: " + orderId));
        if (userId==theOrder.getUser().getId()) {
            int updateCount = orderRepo.updateReviewById(orderId,
                    reqeust.getReview());
            var nextStatus = reqeust.getReview()==null ?
                    OrderStatus.PURCHASE_CONFIRMED:OrderStatus.REVIEWED;
            int statusCount = orderRepo.updateOrderStatusByOrderId(orderId,
                    nextStatus);

            return (updateCount==1 && statusCount==1);
        } else {
            throw new UnauthorizedException(
                    Feedback.NOT_BELONG_TO_YOU + orderId);
        }
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long id, OrderStatus status) {
        int updateCount = orderRepo.updateOrderStatusByOrderId(id, status);
        return (updateCount==1);
    }

    @Transactional
    @Override
    public boolean updateWaybillNoOfId(UpdateWaybillNoReq request)
            throws TrackingMoreException, IOException {

        int count1 = orderRepo.updateWaybillNoById(request.getId(),
                request.getWaybillNo());
        int count2 = orderRepo.updateOrderStatusByOrderId(request.getId(),
                request.getStatus());
        boolean result = count1==1 && count2==1;

        if (result) {
            createTrackingMore(request);
        }
        return result;
    }

    private void createTrackingMore(UpdateWaybillNoReq request)
            throws TrackingMoreException, IOException {

        TrackingMore trackingMore = new TrackingMore(trackingmoreApiKey);
        var createTrackingParams = new CreateTrackingParams();
        String orderNumber = String.valueOf(request.getId());

        createTrackingParams.setOrderNumber(orderNumber);
        createTrackingParams.setTrackingNumber(request.getWaybillNo());
        createTrackingParams.setCourierCode("cjlogistics");

        var tMoreResponse =
                trackingMore.trackings.CreateTracking(createTrackingParams);

        logger.debug("TrackingMore 배송 추적 요청 반응 코드: {}",
                tMoreResponse.getMeta().getCode());

        if (tMoreResponse.getData()!=null) {
            Tracking tracking = (Tracking) tMoreResponse.getData();
            logger.debug("추적: {}", tracking.toString());
            logger.debug("추적번호: {}", tracking.getTrackingNumber());
        }
    }

    @Override
    public OrderDetailDto serviceOrderDetail(Long orderId) {
        var orderInfoDto = orderRepo.findOrderDetail(orderId);
        var orderItems = orderItemRepo.findByOrderId(orderId);
        var orderDto = orderInfoDto.orElseThrow(
                () -> new IdNotFoundEx(Feedback.ORDER_ID_NOT_FOUND + orderId));

        int totalSoapCount = 0;
        for (var item : orderItems) {
            totalSoapCount += item.getCount();
        }
        return new OrderDetailDto(orderDto, orderItems, totalSoapCount);
    }

    @Override
    public ReviewInfo serviceReviewInfo(Long oId) {
        var reviewInfoOpt = orderRepo.findReviewInfo(oId);
        var reviewInfo = reviewInfoOpt.orElseThrow(
                () -> new IdNotFoundEx(Feedback.ORDER_ID_NOT_FOUND + oId));
        return reviewInfo;
    }

    @Override
    public SearchResult<MyOrderDto> serviceMyOrders(long userId,
                                                    Optional<Integer> page,
                                                    Optional<Integer> size) {
        int pageSize = size.orElse(provider.getPageSize());
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, pageSize);
        Page<MyOrderDto> myOrderPage = orderRepo.findMyOrders(userId, pageable);
        int totalPages = myOrderPage.getTotalPages();

        List<Integer> pageNumbers = null;
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        var result = new SearchResult<MyOrderDto>(myOrderPage,
                myOrderPage.getNumber() + 1,
                pageSize,
                totalPages,
                pageNumbers
        );
        return result;
    }

    @Override
    public SearchResult<OrderPageRow> serviceOrderPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<OrderPageRow> myOrderPage = orderRepo.findOrderPage(pageable);
        int totalPages = myOrderPage.getTotalPages();

        List<Integer> pageNumbers = null;
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        var result = new SearchResult<OrderPageRow>(myOrderPage,
                myOrderPage.getNumber() + 1,
                size,
                totalPages,
                pageNumbers
        );
        return result;
    }

    @Override
    public SearchResult<MyReviewRow> serviceMyReviewPage(
            Long uid, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MyReviewRow> myReviewPage = orderRepo.myReviews(uid, pageable);
        int totalPages = myReviewPage.getTotalPages();

        List<Integer> pageNumbers = null;
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        var result = new SearchResult<MyReviewRow>(myReviewPage,
                myReviewPage.getNumber() + 1,
                size,
                totalPages,
                pageNumbers
        );
        return result;
    }

    @Override
    public SearchResult<ReviewRow> serviceReviewPage(
            Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReviewRow> myReviewPage = orderRepo.getReviewPage(pageable);
        int totalPages = myReviewPage.getTotalPages();

        List<Integer> pageNumbers = null;

        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new SearchResult<ReviewRow>(myReviewPage,
                myReviewPage.getNumber() + 1,
                size,
                totalPages,

                pageNumbers
        );
    }

    public BsOrder getOrderByOrderId(String orderId)
            throws OrderIdNotFoundEx {
        var optionalOrder = orderRepo.findByOrderId(orderId);

        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new OrderIdNotFoundEx(Feedback.ORDER_ID_NOT_FOUND + orderId);
        }
    }

    @Override
    @Transactional(rollbackOn = InventoryException.class)
    public BsOrder saveOrder(BsOrder order) {
        // recipient 저장 및 order 속성 변경
        // 상향식 순서로 하위 요소들을 저장하고, 저장 결과로 기존 멤버를 대체!
        var recipient = order.getRecipient();
        var basis = addressBasisServ.addGetAddrBasis(recipient.getAddressBasis());
        recipient.setAddressBasis(basis);

        var recipientSaved = recipientServ.save(recipient);
        order.setRecipient(recipientSaved);
        order.getItems().forEach(item ->
                item.setSubTotal(subTotaler.getSubtotal(item)));
        order.getItems().forEach(item -> item.setOrder(order));

        order.setPayment(calculatePayment(order));

        order.setOrderId("");
        entityManager.persist(order);
        entityManager.flush();

        order.setOrderId(orderIdGenerator.generateOrderId(order));

        return order;
    }

    private BigDecimal calculatePayment(BsOrder order) {
        // 각 항목의 소계를 합한다.
        BigDecimal payment = new BigDecimal(0);
        for (var item : order.getItems()) {
            payment = payment.add(item.getSubTotal());
        }

        // 배송비 계산
        FeeEtc feeEtc = feeEtcServ.readLatest();
        BigDecimal delivery =
                payment.compareTo(feeEtc.getDeliFreeMin()) >= 0
                        ? BigDecimal.ZERO:feeEtc.getDeliBasis();

        boolean isJeju = order.getRecipient().getAddressBasis()
                .getZipcode().startsWith("63");
        return payment.add(
                isJeju ? delivery.add(feeEtc.getDeliJeju()):delivery);
    }

    @Override
    public BigDecimal findDeliveryFee(
            BigDecimal grandTotal, String zipcode) {

        FeeEtc feeEtc = feeEtcServ.readLatest();
        BigDecimal delivery =
                grandTotal.compareTo(feeEtc.getDeliFreeMin()) >= 0
                        ? BigDecimal.ZERO:feeEtc.getDeliBasis();

        return zipcode.startsWith("63")
                ? delivery.add(feeEtc.getDeliJeju()):delivery;
    }

    @Override
    public BsOrder findOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(
                () -> new IdNotFoundEx(Feedback.ORDER_ID_NOT_FOUND + id));
    }

    @Override
    @Transactional(rollbackOn = InventoryException.class)
    public BsOrder updateOrder(BsOrder order) {
        var readOrder = findOrderById(order.getId());
        var idsToKeep = order.getItems().stream()
                .map(OrderItem::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 제거, 새 주문 목록에 없는 기존 목록 항목 제거
        readOrder.getItems().removeIf(
                item -> {
                    var itemId = item.getId();
                    if (idsToKeep.contains(itemId)) {
                        return false;
                    } else {
                        orderItemRepo.deleteById(itemId);
                        return true;
                    }
                });

        // 추가, 새 목록 중, ID가 null 인 항목 수집
        var newItems = order.getItems().stream()
                .filter(item -> item.getId()==null)
                .toList();

        newItems.forEach(item -> {
            item.setOrder(readOrder);
            item.setSubTotal(subTotaler.getSubtotal(item));
        });
        newItems.forEach(item -> readOrder.getItems().add(item));

        // 갱신, 새 목록 중, ID 가 있는 항목에 대해 읽은 주문의 항목에 성질값 복사
        order.getItems().stream()
                .filter(item -> item.getId()!=null)
                .forEach(item ->
                        readOrder.getItems().stream()
                                .filter(readItem -> item.getId().equals(readItem.getId()))
                                .findFirst()
                                .ifPresent(readItem -> {
                                    readItem.assign(item);
                                    readItem.setSubTotal(subTotaler.getSubtotal(readItem));
                                })
                );

        BsOrder savedOrder = orderRepo.save(readOrder);
        return savedOrder;
    }

    @Override
    public void deleteById(Long id) {
        orderRepo.deleteById(id);
    }

    @Override
    public int deleteOrdersByUserIdWithoutPayments(String email) {
        return orderRepo.deleteOrdersByUserEmailWithoutPayments(email);
    }

}
