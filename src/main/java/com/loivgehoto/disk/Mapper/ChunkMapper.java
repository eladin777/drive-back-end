package com.loivgehoto.disk.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loivgehoto.disk.Model.Chunk;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;


public interface ChunkMapper extends BaseMapper<Chunk> {

    public void saveChunk(Chunk chunk);

    public List checkChunk(String identifier,String user_name);

    public void delete_chunk_infor(String identifier);
}
