package com.loivgehoto.disk.Service;

import com.loivgehoto.disk.Mapper.ChunkMapper;
import com.loivgehoto.disk.Model.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ChunkService
{
    @Autowired
    private ChunkMapper chunkMapper;
    public void saveChunk(Chunk chunk)
    {
        chunkMapper.saveChunk(chunk);
    }

    public List checkChunk(String identifier,String user_name)
    {
       return  chunkMapper.checkChunk(identifier,user_name);
    }

    public void delete_chunk_infor(String identifier)
    {
        chunkMapper.delete_chunk_infor(identifier);
    }
}
