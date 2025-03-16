
# BookShop

<div>
  <img width="600" alt="BS_메인화면" src="https://github.com/user-attachments/assets/021c4c08-0535-47ba-b151-456f0da941a4" />
</div>

## 프로젝트 소개
도서 판매 웹 사이트입니다. 
프로젝트 기간	: 2024.5 - 2024.6 1개월 <br>

## 개발 환경

프레임 워크
- Typescript, React
- Zustand, axios
- Java, Spring, MySQL, JWT

## 프로젝트 구조

<details>
  <summary>프론트엔드</summary>
  ```
  📦src
 ┣ 📂api
 ┃ ┣ 📜auth.api.ts
 ┃ ┣ 📜banner.api.ts
 ┃ ┣ 📜books.api.ts
 ┃ ┣ 📜carts.api.ts
 ┃ ┣ 📜category.api.ts
 ┃ ┣ 📜http.ts
 ┃ ┣ 📜order.api.ts
 ┃ ┣ 📜queryClient.ts
 ┃ ┗ 📜review.api.ts
 ┣ 📂assets
 ┃ ┗ 📂images
 ┃ ┃ ┗ 📜logo.png
 ┣ 📂components
 ┃ ┣ 📂book
 ┃ ┃ ┣ 📜AddToCart.tsx
 ┃ ┃ ┣ 📜BookReview.tsx
 ┃ ┃ ┣ 📜BookReviewAdd.tsx
 ┃ ┃ ┣ 📜BookReviewItem.tsx
 ┃ ┃ ┗ 📜LikeButton.tsx
 ┃ ┣ 📂books
 ┃ ┃ ┣ 📜BookBestItem.tsx
 ┃ ┃ ┣ 📜BookItem.spec.tsx
 ┃ ┃ ┣ 📜BookItem.tsx
 ┃ ┃ ┣ 📜BooksEmpty.tsx
 ┃ ┃ ┣ 📜BooksFilter.tsx
 ┃ ┃ ┣ 📜BooksList.tsx
 ┃ ┃ ┣ 📜BooksViewSwitcher.tsx
 ┃ ┃ ┗ 📜Pagination.tsx
 ┃ ┣ 📂cart
 ┃ ┃ ┣ 📜cartItem.tsx
 ┃ ┃ ┣ 📜CartSummary.tsx
 ┃ ┃ ┗ 📜CheckIconButton.tsx
 ┃ ┣ 📂common
 ┃ ┃ ┣ 📂banner
 ┃ ┃ ┃ ┣ 📜Banner.tsx
 ┃ ┃ ┃ ┗ 📜BannerItem.tsx
 ┃ ┃ ┣ 📂toast
 ┃ ┃ ┃ ┣ 📜Toast.tsx
 ┃ ┃ ┃ ┗ 📜toastContainer.tsx
 ┃ ┃ ┣ 📜Button.spec.tsx
 ┃ ┃ ┣ 📜Button.tsx
 ┃ ┃ ┣ 📜Dropdown.tsx
 ┃ ┃ ┣ 📜EllipsisBox.tsx
 ┃ ┃ ┣ 📜Empty.tsx
 ┃ ┃ ┣ 📜Error.tsx
 ┃ ┃ ┣ 📜Footer.tsx
 ┃ ┃ ┣ 📜Header.tsx
 ┃ ┃ ┣ 📜InputText.spec.tsx
 ┃ ┃ ┣ 📜InputText.tsx
 ┃ ┃ ┣ 📜Loading.tsx
 ┃ ┃ ┣ 📜Modal.tsx
 ┃ ┃ ┣ 📜Sidebar.tsx
 ┃ ┃ ┣ 📜Tabs.tsx
 ┃ ┃ ┣ 📜Title.spec.tsx
 ┃ ┃ ┗ 📜Title.tsx
 ┃ ┣ 📂header
 ┃ ┃ ┗ 📜ThemeSwitcher.tsx
 ┃ ┣ 📂layout
 ┃ ┃ ┗ 📜Layout.tsx
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📜MainBest.tsx
 ┃ ┃ ┣ 📜MainNewBooks.tsx
 ┃ ┃ ┗ 📜MainReview.tsx
 ┃ ┗ 📂order
 ┃ ┃ ┗ 📜FindAddressButton.tsx
 ┣ 📂constants
 ┃ ┣ 📜pagination.ts
 ┃ ┗ 📜querystring.ts
 ┣ 📂context
 ┃ ┗ 📜themeContext.tsx
 ┣ 📂hooks
 ┃ ┣ 📜useAlert.ts
 ┃ ┣ 📜useAuth.ts
 ┃ ┣ 📜useBook.ts
 ┃ ┣ 📜useBooks.ts
 ┃ ┣ 📜useBooksInfinite.ts
 ┃ ┣ 📜useCart.ts
 ┃ ┣ 📜useCategory.ts
 ┃ ┣ 📜useIntersectionObserver.ts
 ┃ ┣ 📜useMain.ts
 ┃ ┣ 📜useMediaQuery.ts
 ┃ ┣ 📜useOrders.ts
 ┃ ┣ 📜useTimeout.ts
 ┃ ┗ 📜useToast.ts
 ┣ 📂mock
 ┃ ┣ 📜banner.ts
 ┃ ┣ 📜books.ts
 ┃ ┣ 📜browser.ts
 ┃ ┗ 📜review.ts
 ┣ 📂models
 ┃ ┣ 📜banner.model.ts
 ┃ ┣ 📜book.model.ts
 ┃ ┣ 📜cart.model.ts
 ┃ ┣ 📜category.model.ts
 ┃ ┣ 📜order.model.ts
 ┃ ┣ 📜pagination.model.ts
 ┃ ┗ 📜user.model.ts
 ┣ 📂pages
 ┃ ┣ 📜BookDetail.tsx
 ┃ ┣ 📜Books.tsx
 ┃ ┣ 📜Cart.tsx
 ┃ ┣ 📜Detail.tsx
 ┃ ┣ 📜Home.tsx
 ┃ ┣ 📜Login.tsx
 ┃ ┣ 📜Order.tsx
 ┃ ┣ 📜OrderList.tsx
 ┃ ┣ 📜ResetPassword.tsx
 ┃ ┗ 📜Signup.tsx
 ┣ 📂store
 ┃ ┣ 📜authStore.ts
 ┃ ┗ 📜toastStore.ts
 ┣ 📂style
 ┃ ┣ 📜global.ts
 ┃ ┗ 📜theme.ts
 ┣ 📂utils
 ┃ ┣ 📜format.tsx
 ┃ ┗ 📜image.ts
 ┣ 📜App.test.tsx
 ┣ 📜App.tsx
 ┣ 📜index.tsx
 ┣ 📜logo.svg
 ┣ 📜react-app-env.d.ts
 ┣ 📜reportWebVitals.ts
 ┣ 📜setupTests.ts
 ┗ 📜window.d.ts
  ```
</details>
<details>
  <summary>백엔드</summary>
  ```
  📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂BS
 ┃ ┃ ┃ ┗ 📂spring_BS
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthenticationConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BookController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CartController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LikeController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderController.java
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BookDetailDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BooksResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CartItemRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CartItemResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DeliveryDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberSignInRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberSignUpRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderDetailResponseDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderItemDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderResponseDTO.java
 ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseTimeEntity.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Book.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CartItem.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Category.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Delivery.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Like.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Member.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Order.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderedBook.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜Role.java
 ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtFilter.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtUtil.java
 ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BookRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CartItemRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DeliveryRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LikeRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderedBookRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderRepository.java
 ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┣ 📂impl
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BookService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CartService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LikeService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderService.java
 ┃ ┃ ┃ ┃ ┗ 📜SpringBsApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┗ 📜application.yml
 ┗ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂BS
 ┃ ┃ ┃ ┗ 📂spring_BS
 ┃ ┃ ┃ ┃ ┗ 📜SpringBsApplicationTests.java
  ```
</details>

## 화면 및 기능

### 메인 화면 & 로그인 화면
- 메인 화면은 목업 데이터들을 이용해서 여러 디자인을 적용해 두었습니다.
- 로그인 또는 회원가입을 진행할 수 있습니다.
<img width="400" alt="BS_메인화면" src="https://github.com/user-attachments/assets/d3f83654-3989-436c-b906-1159ffba95de" /><img width="400" alt="BS_로그인" src="https://github.com/user-attachments/assets/9ad59e84-bc24-42a7-a8ef-4c9e8bb8af96" />

