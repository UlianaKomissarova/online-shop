package service.impl;

import dto.DeliveryDto;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.Delivery;
import repository.impl.DeliveryDao;
import service.DeliveryServiceInterface;
import service.mapper.DeliveryFactory;

import java.util.*;

import static service.mapper.DeliveryMapper.toDtoFromDelivery;

@NoArgsConstructor
public class DeliveryService implements DeliveryServiceInterface {
    private final DeliveryDao dao = new DeliveryDao();
    private static DeliveryServiceInterface deliveryService;

    public static synchronized DeliveryServiceInterface getInstance() {
        if (deliveryService == null) {
            deliveryService = new DeliveryService();
        }
        return deliveryService;
    }

    @Override
    public void save(DeliveryDto dto, DeliveryFactory factory) throws NotFoundException {
        Delivery delivery = factory.createDeliveryFromDto(dto);
        dao.save(delivery);
    }

    @Override
    public void update(DeliveryDto dto, DeliveryFactory factory) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }
        getExistingDelivery(dto.getId());

        Delivery deliveryForUpdate = factory.createDeliveryFromDto(dto);
        dao.update(deliveryForUpdate);
    }

    @Override
    public DeliveryDto findById(Integer id) throws NotFoundException {
        Delivery delivery = getExistingDelivery(id);
        return toDtoFromDelivery(delivery);
    }

    @Override
    public List<DeliveryDto> findAll() {
        List<Delivery> deliveries = dao.findAll();
        List<DeliveryDto> dtos = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            dtos.add(toDtoFromDelivery(delivery));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        Delivery delivery = getExistingDelivery(id);
        dao.delete(delivery);
    }

    protected Delivery getExistingDelivery(Integer id) throws NotFoundException {
        return dao.findById(id).orElseThrow(() -> new NotFoundException("Delivery not found."));
    }
}