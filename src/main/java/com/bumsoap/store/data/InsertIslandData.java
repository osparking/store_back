package com.bumsoap.store.data;

import com.bumsoap.store.model.IslandAddress;
import com.bumsoap.store.repository.IslandAddressRepo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class InsertIslandData implements ApplicationListener<ApplicationReadyEvent> {
    private final IslandAddressRepo addressRepo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        insertIslandAddressesIfNotExists();
    }

    private void insertIslandAddressesIfNotExists() {
        // 클래스패스에서 ODS 파일 읽기
        try (InputStream inputStream =
                     new ClassPathResource("island-table.xlsx").getInputStream();
             Workbook workbook =
                     new XSSFWorkbook(inputStream)) { // Apache POI ODFWorkbook

            Sheet sheet = workbook.getSheetAt(0);
            // 데이터 행은 1번부터 (0번은 헤더)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row==null) continue;

                String zipcode = getCellStringValue(row.getCell(0));
                String address = getCellStringValue(row.getCell(1));
                String isJejuStr = getCellStringValue(row.getCell(2));
                String isIslandStr = getCellStringValue(row.getCell(3));

                // 필수 값 누락 시 건너뜀
                if (zipcode==null || zipcode.trim().isEmpty() ||
                        address==null || address.trim().isEmpty()) {
                    continue;
                }

                // 이미 등록된 우편번호인지 확인
                if (addressRepo.findByZipcode(zipcode).isPresent()) {
                    continue;   // 중복 건너뜀
                }

                // Boolean 변환 (Y → true, 그 외 false)
                Boolean isJeju = "Y".equalsIgnoreCase(isJejuStr);
                Boolean isIsland = "Y".equalsIgnoreCase(isIslandStr);

                IslandAddress newAddress = IslandAddress.builder()
                        .zipcode(zipcode.trim())
                        .address(address.trim())
                        .isJeju(isJeju)
                        .isIsland(isIsland)
                        .build();

                addressRepo.save(newAddress);
                // 필요 시 로그 출력
            }
        } catch (Exception e) {
            // 파일 오류 등 예외 처리 (로깅 권장)
            e.printStackTrace();
        }
    }

    // 셀 값을 문자열로 안전하게 추출
    private String getCellStringValue(Cell cell) {
        if (cell==null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // 우편번호가 숫자로 저장된 경우도 대비
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return null;
        }
    }
}