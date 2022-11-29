package com.a3.Factories;

import com.a3.Interfaces.InheritanceMapper;
import com.a3.Mappers.AdministratorMapper;
import com.a3.Mappers.StudentMapper;

import java.sql.Connection;

public class MapperFactory {
    public static InheritanceMapper newMapper(String mapperType, Connection conn){
        switch (mapperType){
            case "student":
                return new StudentMapper(conn);
            case "administrator":
                return new AdministratorMapper(conn);
            default:
                return null;
        }
    }
}
