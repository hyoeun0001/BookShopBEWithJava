package BS.spring_BS.service;

import BS.spring_BS.dto.OrderDetailResponseDTO;
import BS.spring_BS.dto.OrderRequestDTO;
import BS.spring_BS.dto.OrderResponseDTO;
import BS.spring_BS.entity.CartItem;
import BS.spring_BS.entity.Delivery;
import BS.spring_BS.entity.Order;
import BS.spring_BS.entity.OrderedBook;
import BS.spring_BS.repository.CartItemRepository;
import BS.spring_BS.repository.DeliveryRepository;
import BS.spring_BS.repository.OrderRepository;
import BS.spring_BS.repository.OrderedBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderedBookRepository orderedBookRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public ResponseEntity<?> createOrder(OrderRequestDTO request, Long memberId) {

        try {
            // Save delivery
            Delivery delivery = new Delivery();
            delivery.setAddress(request.getDelivery().getAddress());
            delivery.setReceiver(request.getDelivery().getReceiver());
            delivery.setContact(request.getDelivery().getContact());
            Delivery savedDelivery = deliveryRepository.save(delivery);

            // Save order
            Order order = new Order();
            order.setBookTitle(request.getFirstBookTitle());
            order.setTotalQuantity(request.getTotalQuantity());
            order.setTotalPrice(request.getTotalPrice());
            order.setMemberId(memberId);
            order.setDelivery(savedDelivery);
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            Order savedOrder = orderRepository.save(order);

            // Get cart items and save ordered books
            List<CartItem> cartItems = cartItemRepository.findByMemberIdAndIdIn(memberId, request.getItems());
            if (cartItems.isEmpty()) {
                throw new IllegalArgumentException("선택한 장바구니 항목이 존재하지 않습니다.");
            }

            List<OrderedBook> orderedBooks = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderedBook orderedBook = new OrderedBook();
                orderedBook.setOrderId(savedOrder.getId());
                orderedBook.setBookId(item.getBook().getId());
                orderedBook.setQuantity(item.getQuantity());
                orderedBooks.add(orderedBook);
            }
            orderedBookRepository.saveAll(orderedBooks);

            // Delete cart items
            cartItemRepository.deleteAll(cartItems);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            throw e; // 트랜잭션 롤백 유도
        } catch (Exception e) {
            throw new RuntimeException("주문 처리 중 오류가 발생했습니다: " + e.getMessage(), e); // 트랜잭션 롤백 유도
        }
    }

    public ResponseEntity<?> getOrders(Long memberId) {
        logger.info("주문 조회 시작 - memberId: {}", memberId);

        try {
            List<Order> orders = orderRepository.findByMemberId(memberId);
            List<OrderResponseDTO> responseList = orders.stream()
                    .map(order -> OrderResponseDTO.builder()
                            .orderId(order.getId())
                            .createdAt(order.getCreatedAt())
                            .address(order.getDelivery().getAddress())
                            .receiver(order.getDelivery().getReceiver())
                            .contact(order.getDelivery().getContact())
                            .bookTitle(order.getBookTitle())
                            .totalPrice(order.getTotalPrice())
                            .totalQuantity(order.getTotalQuantity())
                            .build())
                    .collect(Collectors.toList());

            logger.info("주문 조회 성공 - memberId: {}, orderCount: {}", memberId, responseList.size());
            return ResponseEntity.ok(responseList); // JSON 배열 직접 반환

        } catch (Exception e) {
            logger.error("주문 조회 중 오류 발생 - memberId: {}, error: {}", memberId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Node.js의 400 응답
        }
    }

    public ResponseEntity<?> getOrderDetail(Long orderId) {
        logger.info("주문 상세 조회 시작 - orderId: {}", orderId);

        try {
            List<OrderDetailResponseDTO> responseList = orderedBookRepository.findOrderDetailsByOrderId(orderId);
            logger.info("주문 상세 조회 성공 - orderId: {}, itemCount: {}", orderId, responseList.size());
            return ResponseEntity.ok(responseList); // JSON 배열 직접 반환

        } catch (Exception e) {
            logger.error("주문 상세 조회 중 오류 발생 - orderId: {}, error: {}", orderId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}