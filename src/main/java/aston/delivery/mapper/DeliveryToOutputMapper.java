package aston.delivery.mapper;

import aston.delivery.dto.output.*;
import aston.delivery.model.*;
import org.springframework.stereotype.Component;

@Component
public class DeliveryToOutputMapper {
    public static DeliveryOutput toOutputFromDelivery(Delivery delivery) {
        if (delivery instanceof PostDelivery) {
            PostDeliveryOutput postDto = new PostDeliveryOutput();
            setCommonPropsForDto(delivery, postDto);
            postDto.setTrackNumber(((PostDelivery) delivery).getTrackNumber());

            return postDto;

        } else if (delivery instanceof CourierDelivery) {
            CourierDeliveryOutput courierDto = new CourierDeliveryOutput();
            setCommonPropsForDto(delivery, courierDto);
            setCourierPropsForDto(delivery, courierDto);

            return courierDto;

        } else if (delivery instanceof ParcelLockerDelivery) {
            ParcelLockerDeliveryOutput plDeliveryDto = new ParcelLockerDeliveryOutput();
            setCommonPropsForDto(delivery, plDeliveryDto);
            setParcelLockerPropsForDto(delivery, plDeliveryDto);

            return plDeliveryDto;
        }

        return null;
    }

    private static void setCommonPropsForDto(Delivery delivery, DeliveryOutput dto) {
        dto.setId(delivery.getId());
        dto.setDate(delivery.getDate());
        dto.setPrice(delivery.getPrice());
        dto.setAddress(delivery.getAddress());
        dto.setOrderId(delivery.getOrder().getId());
    }

    private static void setParcelLockerPropsForDto(Delivery delivery, ParcelLockerDeliveryOutput dto) {
        dto.setBoxNumber(((ParcelLockerDelivery) delivery).getBoxNumber());
        dto.setPin(((ParcelLockerDelivery) delivery).getPin());
        dto.setClosedFrom(((ParcelLockerDelivery) delivery).getClosedFrom());
        dto.setOpenFrom(((ParcelLockerDelivery) delivery).getOpenFrom());
    }

    private static void setCourierPropsForDto(Delivery delivery, CourierDeliveryOutput dto) {
        dto.setDeliverTo(((CourierDelivery) delivery).getDeliverTo());
        dto.setDeliverFrom(((CourierDelivery) delivery).getDeliverFrom());
        dto.setCourierPhone(((CourierDelivery) delivery).getCourierPhone());
    }
}