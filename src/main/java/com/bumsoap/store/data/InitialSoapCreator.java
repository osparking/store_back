package com.bumsoap.store.data;

import com.bumsoap.store.exception.UserTypeNotFouncEx;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.AddProduceReq;
import com.bumsoap.store.service.produce.ProduceServI;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Order(10)
public class InitialSoapCreator implements ApplicationListener<ApplicationReadyEvent> {
    private final ProduceServI produceServ;
    private final UserRepoI userRepo;
    private static final Logger logger =
            LoggerFactory.getLogger(InitialSoapCreator.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        insertSoapsProducedIfNotExists();
    }

    @Transactional
    public void insertSoapsProducedIfNotExists() {
        logger.info("=== insertSoapsProducedIfNotExists START ==="); // 이 부분 추가
        var list = produceServ.getSoapStock();

        if (!list.isEmpty()) {
            return;
        }

        var adminUser = userRepo.findFirstByUserType(UserType.ADMIN)
                .orElseThrow(() -> new UserTypeNotFouncEx(Feedback.NOT_FOUND));
        var workerOne = userRepo.findFirstByUserType(UserType.WORKER)
                .orElseThrow(() -> new UserTypeNotFouncEx(Feedback.NOT_FOUND));

        for (BsShape shape : BsShape.values()) {
            var request = new AddProduceReq(null, shape.label,
                    1000L, LocalDate.now(), workerOne.getId());
            var produce = produceServ.addProduce(adminUser.getId(), request);
            logger.debug(produce.getResult());
        }
    }
}
