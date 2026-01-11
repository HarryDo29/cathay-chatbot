package com.cathay.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

//	@Bean
//	CommandLineRunner testJwt(JwtUtil util) {
//		return args -> {
//			System.out.println("----- BẮT ĐẦU TEST JWT -----");
//
//			// 1. Giả lập dữ liệu
//			String username = "testUser";
//			Map<String, Object> claims = Map.of("role", "ADMIN", "email", "test@gmail.com");
//
//			// 2. Gọi hàm tạo token
//			// (Giả sử hàm generateToken của bạn nhận subject và claims)
//			String token = util.buildToken(claims, username); // Hoặc hàm bạn đã viết
//
//			// 3. In ra kết quả
//			System.out.println("Build token: " + token);
//
//			// 4. Thử giải mã lại xem có đúng không (nếu bạn đã viết hàm validate/extract)
//			Claims claim = util.extractToken(token);
//			System.out.println("Extract token: " + claim);
//			System.out.println("username: " + util.extractClaim(claim, claims1 -> claims1.get("email", String.class)));
//
//			System.out.println("----- KẾT THÚC TEST JWT -----");
//		};
//	}

}
