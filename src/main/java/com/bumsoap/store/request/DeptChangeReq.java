package com.bumsoap.store.request;

import lombok.Data;

@Data
public class DeptChangeReq {
  private String dept;

  public void DeptChangeReq(String dept) {
    this.dept = dept;
  }
}
