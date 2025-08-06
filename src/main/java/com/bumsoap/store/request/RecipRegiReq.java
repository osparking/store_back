package com.bumsoap.store.request;

import com.bumsoap.store.util.DoroZbun;
import lombok.Data;

@Data
public class RecipRegiReq {
  private Long id;
  private String fullName;
  private String mbPhone;
  private AddrBasisAddReq addrBasisAddReq;
  private DoroZbun doroZbun;
  private String addressDetail;

  public RecipRegiReq(String addressDetail,
                      String doroZbun,
                      AddrBasisAddReq addrBasisAddReq,
                      String mbPhone,
                      String fullName) {
    this.addressDetail = addressDetail;
    this.doroZbun = DoroZbun.valueOfLabel(doroZbun);
    this.addrBasisAddReq = addrBasisAddReq;
    this.mbPhone = mbPhone;
    this.fullName = fullName;
  }
}
