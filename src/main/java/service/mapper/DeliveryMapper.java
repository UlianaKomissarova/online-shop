package service.mapper;

import dto.*;
import model.*;

public class DeliveryMapper {
    public static DeliveryDto toDtoFromDelivery(Delivery delivery) {
        if (delivery instanceof PostDelivery) {
            PostDeliveryDto postDto = new PostDeliveryDto();
            setCommonPropsForDto(delivery, postDto);
            postDto.setTrackNumber(((PostDelivery) delivery).getTrackNumber());

            return postDto;

        } else if (delivery instanceof CourierDelivery) {
            CourierDeliveryDto courierDto = new CourierDeliveryDto();
            setCommonPropsForDto(delivery, courierDto);
            setCourierPropsForDto(delivery, courierDto);

            return courierDto;

        } else if (delivery instanceof ParcelLockerDelivery) {
            ParcelLockerDeliveryDto plDeliveryDto = new ParcelLockerDeliveryDto();
            setCommonPropsForDto(delivery, plDeliveryDto);
            setParcelLockerPropsForDto(delivery, plDeliveryDto);

            return plDeliveryDto;
        }

        return null;
    }

    private static void setCommonPropsForDto(Delivery delivery, DeliveryDto dto) {
        dto.setId(delivery.getId());
        dto.setDate(delivery.getDate());
        dto.setPrice(delivery.getPrice());
        dto.setAddress(delivery.getAddress());
        dto.setOrderId(delivery.getOrder().getId());
    }

    private static void setParcelLockerPropsForDto(Delivery delivery, ParcelLockerDeliveryDto dto) {
        dto.setBoxNumber(((ParcelLockerDelivery) delivery).getBoxNumber());
        dto.setPin(((ParcelLockerDelivery) delivery).getPin());
        dto.setClosedFrom(((ParcelLockerDelivery) delivery).getClosedFrom());
        dto.setOpenFrom(((ParcelLockerDelivery) delivery).getOpenFrom());
    }

    private static void setCourierPropsForDto(Delivery delivery, CourierDeliveryDto dto) {
        dto.setDeliverTo(((CourierDelivery) delivery).getDeliverTo());
        dto.setDeliverFrom(((CourierDelivery) delivery).getDeliverFrom());
        dto.setCourierPhone(((CourierDelivery) delivery).getCourierPhone());
    }
}