package com.weatherapi.weatherforecast.location;

import java.util.List;

import com.weatherapi.weatherforecast.common.Location;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface LocationRepository extends CrudRepository<Location, String> {

	@Query("SELECT l FROM Location l WHERE l.trashed = false")
	public List<Location> findUntrashed();

	@Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
	public Location findByCode(String code);

	@Modifying
	@Transactional
	@Query("UPDATE Location SET trashed = true WHERE code = ?1")
	public void trashByCode(String code);
}
