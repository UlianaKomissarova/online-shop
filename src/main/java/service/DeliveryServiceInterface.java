package service;

import dto.DeliveryDto;
import exception.NotFoundException;
import service.mapper.DeliveryFactory;

import java.util.List;

public interface DeliveryServiceInterface {
    void save(DeliveryDto dto, DeliveryFactory deliveryFactory) throws NotFoundException;

    void update(DeliveryDto dto, DeliveryFactory factory) throws NotFoundException;

    DeliveryDto findById(Integer id) throws NotFoundException;

    List<DeliveryDto> findAll();

    void delete(Integer id) throws NotFoundException;
}