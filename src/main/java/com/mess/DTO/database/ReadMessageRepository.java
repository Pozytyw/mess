package com.mess.DTO.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Transactional(readOnly = false)
@Repository("readMessageRepository")
public interface ReadMessageRepository extends JpaRepository<ReadMessageDTO, Conv_userId> {

    @Query(value = "select * from conv_user where conv_id = :conv_id", nativeQuery = true)
    List<ReadMessageDTO> findByConv_id(@Param("conv_id") long conv_id);

    @Query(value = "select * from conv_user where conv_id = :conv_id and user_id = :user_id", nativeQuery = true)
    ReadMessageDTO findById(@Param("conv_id") long conv_id, @Param("user_id") long user_id);

    @Modifying
    @Query(value = "update conv_user set mess_id = :mess_id, read_date = :rd where conv_id = :conv_id and user_id = :user_id",nativeQuery = true)
    void updateRead(@Param("mess_id") long mess_id, @Param("rd") Calendar rd, @Param("conv_id") long conv_id, @Param("user_id") long user_id);
}
