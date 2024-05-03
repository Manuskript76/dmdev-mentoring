package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.enums.OrderStatus;
import com.vmdev.eshop.mapper.ClientOrderMapper;
import com.vmdev.eshop.repository.ClientOrderRepository;
import com.vmdev.eshop.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final ClientOrderMapper clientOrderMapper;
    private final ClientRepository clientRepository;

    public List<ClientOrderDto> findAllByClient(Long clientId) {
        return clientOrderRepository.findAllByClientId(clientId).stream()
                .map(clientOrderMapper::map)
                .toList();
    }

    public Optional<ClientOrderDto> findById(Long id) {
        return clientOrderRepository.findById(id)
                .map(clientOrderMapper::map);
    }

    public Optional<ClientOrderDto> findByClientUsername(String username) {
        return clientOrderRepository.findByClientEmail(username)
                .map(clientOrderMapper::map);
    }

    @Transactional
    public ClientOrderDto create(Long clientId) {
        ClientOrder clientOrder = new ClientOrder();
        Client client = clientRepository.findById(clientId).orElseThrow();
        clientOrder.setOpenDate(LocalDate.now());
        clientOrder.setStatus(OrderStatus.IN_PROGRESS);
        clientOrder.setClient(client);
        clientOrder.setSummaryCost(0);
        clientOrder.setProductCount(0);
        clientOrderRepository.saveAndFlush(clientOrder);

        return clientOrderMapper.map(clientOrder);
    }

    @Transactional
    public Optional<ClientOrderDto> update(ClientOrderDto orderDto) {
        return clientOrderRepository.findById(orderDto.getId())
                .map(order -> clientOrderMapper.map(orderDto))
                .map(clientOrderRepository::saveAndFlush)
                .map(clientOrderMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return clientOrderRepository.findById(id)
                .map(clientOrder -> {
                    clientOrderRepository.delete(clientOrder);
                    clientOrderRepository.flush();
                    return true;
                })
                .orElse(false);
    }


}
