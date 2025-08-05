package com.bumsoap.store.util;

import com.bumsoap.store.model.AddressBasis;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class ZipCode {
  /**
   * 공공데이타포털(http://www.data.go.kr) 오픈 API 이용 서비스명 : 통합검색 5자리 우편번호
   * 조회서비스 새 우편번호(2015-08-01부터) 오픈 API 주소
   *
   * @param addrSearchKey : 주소 검색어 (도로명주소[도로명/건물명] 또는 지번주소[동/읍/면/리])
   * @param pageIndex     : 읽을 페이지 번호(1: 첫 페이지)
   * @param pageSize      : 페이지 크기(최대 50)
   * @param pageAddresses : 읽은 주소(우편번호, 도로주소, 지번주소) 한 페이지
   * @param result        : 검색 결과[전체 주소항 개수, 읽은 페이지 번호]
   * @return 오류 메시지, 성공한 경우 null
   */
  public static String find(String addrSearchKey, int pageIndex, int pageSize,
                            List<AddressBasis> pageAddresses, int[] result) {
    HttpURLConnection con = null;
    var urlBuilder = new StringBuilder("http://openapi.epost.go.kr/");

    urlBuilder.append("postal/retrieveNewAdressAreaCdSearchAllService/");
    urlBuilder.append("retrieveNewAdressAreaCdSearchAllService/");
    urlBuilder.append("getNewAddressListAreaCdSearchAll");
    urlBuilder.append("?ServiceKey=");
    urlBuilder.append(System.getenv("ZIP_SEARCH_APIKEY")); // API 인증키
    urlBuilder.append("&countPerPage=");
    urlBuilder.append(pageSize); // 페이지당 출력될 개수를 지정(최대 50)
    urlBuilder.append("&currentPage=");
    urlBuilder.append(pageIndex); // 출력될 페이지 번호
    urlBuilder.append("&srchwrd=");

    try {
      urlBuilder.append(URLEncoder.encode(addrSearchKey, "UTF-8")); // 주소 검색어
      URL url = new URL(urlBuilder.toString());

      con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty("Accept-language", "ko");

      DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
      DocumentBuilder bd = fac.newDocumentBuilder();
      Document doc = bd.parse(con.getInputStream());

      boolean bOk = false; // <successYN>Y</successYN> 획득 여부
      addrSearchKey = null; // 에러 메시지

      String nn;
      Node nd;
      NodeList ns = doc.getElementsByTagName("cmmMsgHeader");
      if (ns.getLength() > 0) {
        for (nd = ns.item(0).getFirstChild(); nd != null; nd = nd
            .getNextSibling()) {
          nn = nd.getNodeName();

          if (!bOk) {
            if (nn.equals("successYN")) // 성공 여부
            {
              if (nd.getTextContent().equals("Y"))
                bOk = true; // 검색 성공
            } else if (nn.equals("errMsg")) // 에러 메시지
            {
              addrSearchKey = nd.getTextContent();
            }
          } else {
            if (nn.equals("totalCount")) // 전체 검색수
            {
              result[0] = Integer.parseInt(nd.getTextContent());
            } else if (nn.equals("currentPage")) // 현재 페이지 번호
            {
              result[1] = Integer.parseInt(nd.getTextContent());
            }
          }
        }
      }

      if (bOk) {
        ns = doc.getElementsByTagName("newAddressListAreaCdSearchAll");
        for (pageIndex = 0; pageIndex < ns.getLength(); pageIndex++) {
          nd = ns.item(pageIndex).getFirstChild();
          String zipcode = nd.getTextContent();
          nd = nd.getNextSibling();
          String roadAddress = nd.getTextContent();
          nd = nd.getNextSibling();
          String zBunAddress = nd.getTextContent();
          pageAddresses
              .add(new AddressBasis(zipcode, roadAddress, zBunAddress));
        }
      }

      if (addrSearchKey == null) { // OK!
        if (pageAddresses.size() < 3)
          addrSearchKey = "검색결과가 없습니다.";
      }
    } catch (Exception e) {
      addrSearchKey = e.getMessage();
    }

    if (con != null)
      con.disconnect();

    return addrSearchKey;
  }
}
