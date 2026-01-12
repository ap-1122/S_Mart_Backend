 package com.example.demo.service;

import com.example.demo.dto.AttributeRequestDto;
import com.example.demo.dto.AttributeResponseDto;
import com.example.demo.model.Attribute;
import com.example.demo.model.AttributeValue;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.repository.AttributeValueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository; // Ensure ye Repo bani ho (Phase 1 me thi)

    @Autowired
    private ModelMapper modelMapper;

    public AttributeResponseDto createAttribute(AttributeRequestDto dto) {
        // 1. Attribute Save karo (e.g. Color)
        Attribute attribute = new Attribute();
        attribute.setName(dto.getName());
        Attribute savedAttr = attributeRepository.save(attribute);

        // 2. Values Save karo (e.g. Red, Blue)
        if (dto.getValues() != null) {
            for (String val : dto.getValues()) {
                AttributeValue av = new AttributeValue();
                av.setAttribute(savedAttr);
                av.setValue(val);
                attributeValueRepository.save(av);
            }
        }
        
        return getAttributeById(savedAttr.getId());
    }

    public List<AttributeResponseDto> getAllAttributes() {
        return attributeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AttributeResponseDto getAttributeById(Long id) {
        Attribute attr = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        return mapToDto(attr);
    }

    // Helper: Entity -> DTO Convert
    private AttributeResponseDto mapToDto(Attribute attr) {
        AttributeResponseDto dto = modelMapper.map(attr, AttributeResponseDto.class);
        // Values ko manually list me convert karke DTO me dalo
        List<String> values = attr.getValues().stream()
                .map(AttributeValue::getValue)
                .collect(Collectors.toList());
        dto.setValues(values);
        return dto;
    }
}