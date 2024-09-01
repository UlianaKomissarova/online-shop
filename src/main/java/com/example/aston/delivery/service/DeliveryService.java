package com.example.aston.delivery.service;

import com.example.aston.delivery.dto.input.CourierDeliveryInput;
import com.example.aston.delivery.dto.input.DeliveryInput;
import com.example.aston.delivery.dto.input.ParcelLockerDeliveryInput;
import com.example.aston.delivery.dto.input.PostDeliveryInput;
import com.example.aston.delivery.dto.output.DeliveryOutput;
import com.example.aston.delivery.mapper.DeliveryFactory;
import com.example.aston.delivery.mapper.DeliveryToOutputMapper;
import com.example.aston.delivery.model.CourierDelivery;
import com.example.aston.delivery.model.Delivery;
import com.example.aston.delivery.model.ParcelLockerDelivery;
import com.example.aston.delivery.model.PostDelivery;
import com.example.aston.delivery.repository.DeliveryRepository;
import com.example.aston.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.aston.delivery.mapper.DeliveryToOutputMapper.toOutputFromDelivery;

@Service
public class DeliveryService implements DeliveryServiceInterface {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryFactory deliveryFactory;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, DeliveryFactory deliveryFactory) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryFactory = deliveryFactory;
    }

    @Override
    @Transactional
    public DeliveryOutput save(DeliveryInput dto) throws NotFoundException {
        Delivery delivery = deliveryFactory.createDeliveryFromDto(dto);
        delivery = deliveryRepository.save(delivery);

        return toOutputFromDelivery(delivery);
    }

    @Override
    @Transactional
    public DeliveryOutput update(DeliveryInput dto, Integer id) throws NotFoundException {
        Delivery delivery = getExistingDelivery(id);
        updateDeliveryProps(dto, delivery);
        delivery = deliveryRepository.save(delivery);

        return toOutputFromDelivery(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryOutput findById(Integer id) throws NotFoundException {
        Delivery delivery = getExistingDelivery(id);
        return toOutputFromDelivery(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryOutput> findAll(String type) {
        return switch (type) {
            case "post" -> deliveryRepository.findAllPostDeliveries().stream()
                .map(DeliveryToOutputMapper::toOutputFromDelivery)
                .collect(Collectors.toList());
            case "parcel_locker" -> deliveryRepository.findAllParcelLockerDeliveries().stream()
                .map(DeliveryToOutputMapper::toOutputFromDelivery)
                .collect(Collectors.toList());
            case "courier" -> deliveryRepository.findAllCourierDeliveries().stream()
                .map(DeliveryToOutputMapper::toOutputFromDelivery)
                .collect(Collectors.toList());
            default -> deliveryRepository.findAll().stream()
                .map(DeliveryToOutputMapper::toOutputFromDelivery)
                .collect(Collectors.toList());
        };
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        Delivery delivery = getExistingDelivery(id);
        deliveryRepository.delete(delivery);
    }

    protected Delivery getExistingDelivery(Integer id) throws NotFoundException {
        return deliveryRepository.findById(id).orElseThrow(() -> new NotFoundException("Delivery not found."));
    }

    private void updateDeliveryProps(DeliveryInput dto, Delivery delivery) {
        switch (dto.getType()) {
            case "post" -> {
                if (dto instanceof PostDeliveryInput && delivery instanceof PostDelivery) {
                    updateCommonProps(dto, delivery);

                    if (((PostDeliveryInput) dto).getTrackNumber() != null &&
                        !((PostDeliveryInput) dto).getTrackNumber().isBlank()) {
                        ((PostDelivery) delivery).setTrackNumber(((PostDeliveryInput) dto).getTrackNumber());
                    }
                } else {
                    throw new IllegalArgumentException("Invalid DTO type for post delivery.");
                }
            }
            case "courier" -> {
                if (dto instanceof CourierDeliveryInput && delivery instanceof CourierDelivery) {
                    updateCommonProps(dto, delivery);
                    updatePropsForCourierDelivery((CourierDeliveryInput) dto, (CourierDelivery) delivery);
                } else {
                    throw new IllegalArgumentException("Invalid DTO type for courier delivery.");
                }
            }
            case "parcel_locker" -> {
                if (dto instanceof ParcelLockerDeliveryInput && delivery instanceof ParcelLockerDelivery) {
                    updateCommonProps(dto, delivery);
                    updatePropsForParcelLockerDelivery((ParcelLockerDeliveryInput) dto, (ParcelLockerDelivery) delivery);
                } else {
                    throw new IllegalArgumentException("Invalid DTO type for parcel locker delivery.");
                }
            }
            default -> throw new IllegalArgumentException("Invalid DTO type.");
        }
    }

    private void updateCommonProps(DeliveryInput dto, Delivery delivery) {
        if (dto.getDate() != null && dto.getDate().after(Date.valueOf(LocalDate.now()))) {
            delivery.setDate(dto.getDate());
        }

        if (dto.getPrice() != null) {
            delivery.setPrice(dto.getPrice());
        }

        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            delivery.setAddress(dto.getAddress());
        }
    }

    private void updatePropsForCourierDelivery(CourierDeliveryInput dto, CourierDelivery delivery) {
        if (dto.getCourierPhone() != null && !dto.getCourierPhone().isBlank()) {
            delivery.setCourierPhone(dto.getCourierPhone());
        }

        if (dto.getDeliverTo() != null) {
            delivery.setDeliverTo(dto.getDeliverTo());
        }

        if (dto.getDeliverFrom() != null) {
            delivery.setDeliverFrom(dto.getDeliverFrom());
        }
    }

    private void updatePropsForParcelLockerDelivery(ParcelLockerDeliveryInput dto, ParcelLockerDelivery delivery) {
        if (dto.getBoxNumber() > 0) {
            delivery.setBoxNumber(dto.getBoxNumber());
        }

        if (dto.getPin() != null && !dto.getPin().isBlank()) {
            delivery.setPin(dto.getPin());
        }

        if (dto.getOpenFrom() != null) {
            delivery.setOpenFrom(dto.getOpenFrom());
        }

        if (dto.getClosedFrom() != null) {
            delivery.setClosedFrom(dto.getClosedFrom());
        }
    }
}