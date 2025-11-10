package com.sampoom.backend.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorStatus {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 10400),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", 10401),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", 10406),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다.", 10404),

    // 409 CONFLICT
    CONFLICT(HttpStatus.CONFLICT, "충돌이 발생했습니다.", 10409),


    // 400 BAD_REQUEST
    SHORT_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "서명용 공개키의 길이가 짧습니다. 적어도 2048비트 이상으로 설정하세요.", 12401),
    NULL_BLANK_TOKEN(HttpStatus.BAD_REQUEST, "토큰 값은 Null 또는 공백이면 안됩니다.", 12400),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력 값입니다.", 11402),
    INVALID_WORKSPACE_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 조직(workspace) 타입입니다.", 11401),
    BLANK_TOKEN_ROLE(HttpStatus.BAD_REQUEST,"토큰 내 권한 정보가 공백입니다.",12404),
    NULL_TOKEN_ROLE(HttpStatus.BAD_REQUEST,"토큰 내 권한 정보가 NULL입니다.",12405),
    INVALID_REQUEST_ORGID(HttpStatus.BAD_REQUEST,"workspace 없이 organizationID로만 요청할 수 없습니다.",11403),
    INVALID_EMPSTATUS_TYPE(HttpStatus.BAD_REQUEST,"유효하지 않은 직원 상태(EmployeeStatus) 타입입니다.",11404),
    INVALID_PUBLIC_KEY(HttpStatus.BAD_REQUEST,"서명용 공개키가 유효하지 않거나 불러오는데 실패했습니다.",12406),

    // 401 UNAUTHORIZED
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", 12410),
    NOT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"토큰의 타입이 액세스 토큰이 아닙니다.",12413),
    NOT_SERVICE_TOKEN(HttpStatus.UNAUTHORIZED,"토큰의 타입이 서비스 토큰(내부 통신용 토큰)이 아닙니다.",12414),
    INVALID_TOKEN_ROLE(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰 내 권한 정보입니다. (토큰 권한 불일치)",12415),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", 12411),

    // 403 FORBIDDEN
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"접근 권한이 없어 접근이 거부되었습니다.",11430),

    // 404 NOT_FOUND
    NOT_FOUND_USER_BY_ID(HttpStatus.NOT_FOUND, "유저 고유 번호(userId)로 해당 유저를 찾을 수 없습니다.", 11440),
    NOT_FOUND_FACTORY_NAME(HttpStatus.NOT_FOUND, "지점명으로 공장 이름을 찾을 수 없습니다.", 10440),
    NOT_FOUND_WAREHOUSE_NAME(HttpStatus.NOT_FOUND, "지점명으로 창고 이름을 찾을 수 없습니다.", 10441),
    NOT_FOUND_AGENCY_NAME(HttpStatus.NOT_FOUND, "지점명으로 대리점 이름을 찾을 수 없습니다.", 10442),
    NOT_FOUND_FACTORY_EMPLOYEE(HttpStatus.NOT_FOUND,"전체 공장에서 해당 직원을 찾을 수 없습니다.",11442),
    NOT_FOUND_WAREHOUSE_EMPLOYEE(HttpStatus.NOT_FOUND,"전체 창고에서 해당 직원을 찾을 수 없습니다.",11443),
    NOT_FOUND_AGENCY_EMPLOYEE(HttpStatus.NOT_FOUND,"전체 대리점에서 해당 직원을 찾을 수 없습니다.",11444),

    // 409 CONFLICT
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 존재하는 유저의 ID입니다.", 11491),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", 10500),
    INVALID_EVENT_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 형식이 유효하지 않습니다.", 10501),
    FAILED_CONNECTION_KAFKA(HttpStatus.INTERNAL_SERVER_ERROR,"Kafka 브로커 연결 또는 통신에 실패했습니다.",10503),
    EVENT_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Kafka 이벤트 처리 중 예외가 발생했습니다.",10504),
    OUTBOX_SERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Outbox 직렬화에 실패했습니다.",10505),

    // 대리점
    AGENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대리점을 찾을 수 없습니다.", 60100),

    // 직원
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "직원을 찾을 수 없습니다.", 60110),
    EMPLOYEE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 직원입니다.", 60111),
    EMPLOYEE_FORBIDDEN(HttpStatus.FORBIDDEN, "직원 관리 권한이 없습니다.", 60112),
    EMPLOYEE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "직원 정보에 접근할 수 없습니다.", 60113),

    // 장바구니
    AGENCY_CART_MISMATCH(HttpStatus.BAD_REQUEST, "해당 대리점의 장바구니 항목이 아닙니다.", 60120),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 항목을 찾을 수 없습니다.", 60121),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "장바구니가 비어 있습니다.", 60122),

    // 주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.", 61100),
    ORDER_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다.", 61101),
    ORDER_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 완료된 주문입니다.", 61102),
    ORDER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "주문 상태가 잘못 되었습니다.", 61103),

    // 출고
    OUTBOUND_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "출고 항목을 찾을 수 없습니다.", 62100),

    // 부품
    PART_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 부품을 찾을 수 없습니다.", 63100),
    STOCK_INSUFFICIENT(HttpStatus.BAD_REQUEST, "재고 수량이 부족합니다.", 63110),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 0보다 커야 합니다.", 63111),
    STOCK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 대리점 재고 항목입니다.", 63112),

    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    public int getStatusCode() {
        return this.httpStatus.value();
    }

}
