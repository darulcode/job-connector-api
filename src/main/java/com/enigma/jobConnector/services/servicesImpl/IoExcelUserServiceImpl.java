package com.enigma.jobConnector.services.servicesImpl;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.UserRole;
import com.enigma.jobConnector.dto.request.UserCategoryRequest;
import com.enigma.jobConnector.entity.User;
import com.enigma.jobConnector.entity.UserCategory;
import com.enigma.jobConnector.services.IoExcelUserService;
import com.enigma.jobConnector.services.UserCategoryService;
import com.enigma.jobConnector.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IoExcelUserServiceImpl implements IoExcelUserService {

    private final UserService userService;
    private final UserCategoryService userCategoryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void importExcelUserData(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_EXCEL_FILE);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<User> users = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String username = row.getCell(2).getStringCellValue();
                String password = passwordEncoder.encode(row.getCell(3).getStringCellValue());
                UserRole role = UserRole.fromDescription(row.getCell(4).getStringCellValue());

                String categoryName = row.getCell(5).getStringCellValue();
                UserCategory userCategory = userCategoryService.getByName(categoryName);
                if (userCategory == null) {
                    userCategoryService.createUserCategory(UserCategoryRequest.builder().name(categoryName).build());
                    userCategory = userCategoryService.getByName(categoryName);
                }

                User user = User.builder()
                        .name(name)
                        .email(email)
                        .username(username)
                        .password(password)
                        .role(role)
                        .userCategory(userCategory)
                        .build();

                users.add(user);
            }

            userService.batchCreate(users);

        } catch (Exception e) {
            throw new RuntimeException(Constant.FAILED_PROCESS_EXCEL_FILE, e);
        }
    }

    @Override
    public void exportExcelUserData(HttpServletResponse response) {
        try {
            List<User> users = userService.findAll();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Users");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("id");
            headerRow.createCell(1).setCellValue("name");
            headerRow.createCell(2).setCellValue("email");
            headerRow.createCell(3).setCellValue("username");
            headerRow.createCell(4).setCellValue("role");
            headerRow.createCell(5).setCellValue("user_category");

            int rowIndex = 1;
            for (User user : users) {
                if (user.getRole() == UserRole.ROLE_SUPER_ADMIN) {
                    continue;
                }
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getUsername());
                row.createCell(4).setCellValue(user.getRole().getDescription());

                if (user.getUserCategory() != null) {
                    row.createCell(5).setCellValue(user.getUserCategory().getName());
                } else {
                    row.createCell(5).setCellValue("");
                }
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=persons.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(Constant.FAILED_PROCESS_EXCEL_FILE, e);
        }
    }
}
