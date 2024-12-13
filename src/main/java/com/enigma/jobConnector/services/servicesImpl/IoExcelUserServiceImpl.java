package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.constants.UserStatus;
import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.dto.response.ImportUserResponse;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.entity.UserCategory;
import com.enigma.jobConnector.services.IoExcelUserService;
import com.enigma.jobConnector.services.UserCategoryService;
import com.enigma.jobConnector.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IoExcelUserServiceImpl implements IoExcelUserService {

    private final UserService userService;
    private final UserCategoryService userCategoryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ImportUserResponse importExcelUserData(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_EXCEL_FILE);
        }

        String message = "";

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Integer sheetCount = workbook.getNumberOfSheets();

            List<User> users = new ArrayList<>();

            for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String email = row.getCell(1).getStringCellValue();
                    if (userService.isUserExistByEmail(email)) {
                        message = message + String.format(Constant.FAILED_IMPORT_USER_EMAIL_ALREADY_EXIST, sheet.getSheetName(), row.getRowNum(), email);
                        continue;
                    }

                    String name = row.getCell(0).getStringCellValue();
                    String password = passwordEncoder.encode(row.getCell(2).getStringCellValue());
                    Cell phoneNumberCell = row.getCell(3);
                    String phoneNumber = null;
                    if (phoneNumberCell != null) {
                        if (phoneNumberCell.getCellType() == CellType.NUMERIC) {
                            phoneNumber = String.valueOf((long) phoneNumberCell.getNumericCellValue());
                        } else if (phoneNumberCell.getCellType() == CellType.STRING) {
                            phoneNumber = phoneNumberCell.getStringCellValue().trim();
                        }
                    }
                    UserRole role = UserRole.fromDescription(row.getCell(4).getStringCellValue());
                    UserCategory userCategory = null;
                    if (!role.equals(UserRole.ROLE_ADMIN) && !role.equals(UserRole.ROLE_SUPER_ADMIN)) {
                        String categoryName = row.getCell(5).getStringCellValue();
                        userCategory = userCategoryService.getByName(categoryName);
                        if (userCategory == null) {
                            userCategoryService.createUserCategory(UserCategoryRequest.builder().name(categoryName).build());
                            userCategory = userCategoryService.getByName(categoryName);
                        }
                    }

                    User user = User.builder()
                            .name(name)
                            .email(email)
                            .phoneNumber(phoneNumber)
                            .password(password)
                            .role(role)
                            .userCategory(userCategory)
                            .status(UserStatus.AKTIVE)
                            .build();

                    users.add(user);
                }
            }
            if (message.isBlank()) userService.batchCreate(users);
            return ImportUserResponse.builder()
                    .message(message)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(Constant.FAILED_PROCESS_EXCEL_FILE, e);
        }
    }

    @Override
    public void exportExcelUserData(HttpServletResponse response) {
        try {
            Workbook workbook = new XSSFWorkbook();

            Map<String, List<User>> usersGroupedByUserCategory = userService.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(user ->
                            user.getUserCategory() != null ? user.getUserCategory().getName() : "Others"));

            for (Map.Entry<String, List<User>> entry : usersGroupedByUserCategory.entrySet()) {
                Sheet sheet = workbook.createSheet(entry.getKey());
                List<User> users = entry.getValue();

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("name");
                headerRow.createCell(1).setCellValue("email");
                headerRow.createCell(2).setCellValue("default_password");
                headerRow.createCell(3).setCellValue("phone_number");
                headerRow.createCell(4).setCellValue("role");
                headerRow.createCell(5).setCellValue("user_category");

                int rowIndex = 1;
                for (User user : users) {
                    if (user.getRole() == UserRole.ROLE_SUPER_ADMIN) {
                        continue;
                    }
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(user.getName());
                    row.createCell(1).setCellValue(user.getEmail());
                    row.createCell(2).setCellValue("password");
                    row.createCell(3).setCellValue(user.getPhoneNumber());
                    row.createCell(4).setCellValue(user.getRole().getDescription());

                    if (user.getUserCategory() != null) {
                        row.createCell(5).setCellValue(user.getUserCategory().getName());
                    } else {
                        row.createCell(5).setCellValue("");
                    }
                }
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=data-users.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(Constant.FAILED_PROCESS_EXCEL_FILE, e);
        }
    }
}
