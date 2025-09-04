package com.medexpress.consult.mapper;

import com.medexpress.consult.base.BaseMapper;
import com.medexpress.consult.domain.Consultation;
import com.medexpress.consult.transfer.resource.ConsultationResource;
import org.mapstruct.Mapper;

/**
 * Mapper class that allows MapStruct to create an implementation of it.
 */
@Mapper(componentModel = "spring")
public interface ConsultationMapper extends BaseMapper<Consultation, ConsultationResource> {
}
