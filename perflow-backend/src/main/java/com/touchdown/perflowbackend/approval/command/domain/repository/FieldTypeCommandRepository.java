package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.FieldType;

import java.util.List;

public interface FieldTypeCommandRepository {

    List<FieldType> findAllByFieldTypeIdIn(List<Long> fieldTypes);
}
