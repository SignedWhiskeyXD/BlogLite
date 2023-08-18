package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.ImageMapping;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface ImageMapper {

    @Select("SELECT * FROM image_mapping WHERE id = #{imgID}")
    ImageMapping selectImageByID(int imgID);

    @Select("SELECT id FROM image_mapping WHERE md5 = #{md5}")
    Integer selectImageByMD5(String md5);

    @Insert("INSERT INTO image_mapping (source, md5, folder, original_name, type_suffix) VALUES" +
            "(#{source}, #{md5}, #{folder}, #{originalName}, #{typeSuffix})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",
            keyProperty="id", before=false, resultType=Integer.class)
    void insertImageMapping(ImageMapping imageMapping);
}
