package com.example.app.motidron.Model.DAO;


import com.example.app.motidron.Model.Conexion;
import com.example.app.motidron.Model.DTO.ReconocimientoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReconocimientoDAO {

    public ReconocimientoDAO(){

    }
    public int registrarReconocimiento(ReconocimientoDTO dto) throws Exception{
        int exito = 0;
        Connection con = Conexion.generarConexion();
        if (con != null) {
            PreparedStatement stmt = con.prepareStatement("insert into reconocimiento "
                    + "(src, latitud, longitud, api_cloud, msg)"
                    + " values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setBytes(1, dto.getSrc());
            stmt.setString(2, dto.getLatitud());
            stmt.setString(3, dto.getLongitud());
            stmt.setString(4, dto.getApi_cloud());
            stmt.setString(5, dto.getMsg());
            try {
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                exito = rs.getInt(1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                con.close();
                stmt.close();
            }
        }
        return exito;
    }

}
