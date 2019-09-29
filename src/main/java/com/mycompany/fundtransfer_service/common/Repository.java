package com.mycompany.fundtransfer_service.common;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {
	int size();

	Optional<T> getById(Long id);
	
	Collection<T> getAll();


}
