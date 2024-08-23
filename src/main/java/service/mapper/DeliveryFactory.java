package service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.*;
import exception.NotFoundException;
import model.*;
import service.impl.OrderService;

import java.io.IOException;

public class DeliveryFactory {
    protected final ObjectMapper objectMapper;
    private static final OrderService orderService = (OrderService) OrderService.getInstance();

    public DeliveryFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DeliveryDto createDeliveryDto(String type, String json) throws IOException {
        if ("post".equalsIgnoreCase(type)) {
            return objectMapper.readValue(json, PostDeliveryDto.class);
        } else if ("parcel_locker".equalsIgnoreCase(type)) {
            return objectMapper.readValue(json, ParcelLockerDeliveryDto.class);
        } else if ("courier".equalsIgnoreCase(type)) {
            return objectMapper.readValue(json, CourierDeliveryDto.class);
        } else {
            throw new IllegalArgumentException("Invalid delivery type.");
        }
    }

    public Delivery createDeliveryFromDto(DeliveryDto dto) throws NotFoundException {
        Delivery delivery;

        if (dto instanceof PostDeliveryDto) {
            delivery = new PostDelivery();
            setCommonPropsForDelivery(delivery, dto);
            ((PostDelivery) delivery).setTrackNumber(((PostDeliveryDto) dto).getTrackNumber());

            return delivery;

        } else if (dto instanceof CourierDeliveryDto) {
            delivery = new CourierDelivery();
            setCommonPropsForDelivery(delivery, dto);
            setCourierPropsForDelivery((CourierDelivery) delivery, dto);

            return delivery;

        } else if (dto instanceof ParcelLockerDeliveryDto) {
            delivery = new ParcelLockerDelivery();
            setCommonPropsForDelivery(delivery, dto);
            setParcelLockerPropsForDelivery((ParcelLockerDelivery) delivery, dto);

            return delivery;
        }

        throw new IllegalArgumentException("Invalid DTO type.");
    }

    private static void setCommonPropsForDelivery(Delivery delivery, DeliveryDto dto) throws NotFoundException {
        delivery.setAddress(dto.getAddress());
        delivery.setId(dto.getId());
        delivery.setDate(dto.getDate());
        delivery.setPrice(dto.getPrice());

        Order order = orderService.getExistingOrder(dto.getOrderId());
        delivery.setOrder(order);
    }

    private static void setParcelLockerPropsForDelivery(ParcelLockerDelivery delivery, DeliveryDto dto) {
        delivery.setPin(((ParcelLockerDeliveryDto) dto).getPin());
        delivery.setClosedFrom(((ParcelLockerDeliveryDto) dto).getClosedFrom());
        delivery.setOpenFrom(((ParcelLockerDeliveryDto) dto).getOpenFrom());
        delivery.setBoxNumber(((ParcelLockerDeliveryDto) dto).getBoxNumber());
    }

    private static void setCourierPropsForDelivery(CourierDelivery delivery, DeliveryDto dto) {
        delivery.setCourierPhone(((CourierDeliveryDto) dto).getCourierPhone());
        delivery.setDeliverFrom(((CourierDeliveryDto) dto).getDeliverFrom());
        delivery.setDeliverTo(((CourierDeliveryDto) dto).getDeliverTo());
    }
}