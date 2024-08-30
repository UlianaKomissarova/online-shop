package aston.delivery.mapper;

import aston.delivery.dto.input.*;
import aston.delivery.model.*;
import aston.exception.NotFoundException;
import aston.order.model.Order;
import aston.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryFactory {
    private final OrderService orderService;

    public Delivery createDeliveryFromDto(DeliveryInput dto) throws NotFoundException {
        Delivery delivery;

        switch (dto.getType()) {
            case "post" -> {
                if (dto instanceof PostDeliveryInput) {
                    delivery = new PostDelivery();
                    setCommonPropsForDelivery(delivery, dto);
                    ((PostDelivery) delivery).setTrackNumber(((PostDeliveryInput) dto).getTrackNumber());
                } else {
                    throw new IllegalArgumentException("Invalid DTO type for post delivery.");
                }
            }
            case "courier" -> {
                if (dto instanceof CourierDeliveryInput) {
                    delivery = new CourierDelivery();
                    setCommonPropsForDelivery(delivery, dto);
                    setCourierPropsForDelivery((CourierDelivery) delivery, dto);
                } else {
                    throw new IllegalArgumentException("Invalid DTO type for courier delivery.");
                }
            }
            case "parcel_locker" -> {
                if (dto instanceof ParcelLockerDeliveryInput) {
                    delivery = new ParcelLockerDelivery();
                    setCommonPropsForDelivery(delivery, dto);
                    setParcelLockerPropsForDelivery((ParcelLockerDelivery) delivery, dto);
                } else {
                    throw new IllegalArgumentException("Invalid DTO type for parcel locker delivery.");
                }
            }
            default -> throw new IllegalArgumentException("Invalid DTO type.");
        }

        return delivery;
    }

    private void setCommonPropsForDelivery(Delivery delivery, DeliveryInput dto) throws NotFoundException {
        delivery.setAddress(dto.getAddress());
        delivery.setId(dto.getId());
        delivery.setDate(dto.getDate());
        delivery.setPrice(dto.getPrice());

        Order order = orderService.getExistingOrder(dto.getOrderId());
        delivery.setOrder(order);
    }

    private void setParcelLockerPropsForDelivery(ParcelLockerDelivery delivery, DeliveryInput dto) {
        delivery.setPin(((ParcelLockerDeliveryInput) dto).getPin());
        delivery.setClosedFrom(((ParcelLockerDeliveryInput) dto).getClosedFrom());
        delivery.setOpenFrom(((ParcelLockerDeliveryInput) dto).getOpenFrom());
        delivery.setBoxNumber(((ParcelLockerDeliveryInput) dto).getBoxNumber());
    }

    private void setCourierPropsForDelivery(CourierDelivery delivery, DeliveryInput dto) {
        delivery.setCourierPhone(((CourierDeliveryInput) dto).getCourierPhone());
        delivery.setDeliverFrom(((CourierDeliveryInput) dto).getDeliverFrom());
        delivery.setDeliverTo(((CourierDeliveryInput) dto).getDeliverTo());
    }
}