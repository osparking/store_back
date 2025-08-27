package com.bumsoap.store.request;

import com.bumsoap.store.dto.CartItemCount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/* 요청 Json 사례:
   { "deleteId": [53],
     "updateCount": [
        {"id": 54, "count": 5 },
        {"id": 56, "count": 2 }]
   }
*/
public class CartUpdateReq {
  private Long[] deleteId;
  private CartItemCount[] updateCount;
}