package com.hotel.hotel_management.util;

import com.hotel.hotel_management.domain.*;
import com.hotel.hotel_management.dto.*;

public class Mappers {

    public static RoomDtos.Response toResponse(Room r) {
        RoomDtos.Response out = new RoomDtos.Response();
        out.setId(r.getId());
        out.setNumber(r.getNumber());
        out.setType(r.getType());
        out.setPrice(r.getPrice());
        out.setStatus(r.getStatus());
        return out;
    }

    public static EmployeeDtos.Response toResponse(Employee e) {
        EmployeeDtos.Response out = new EmployeeDtos.Response();
        out.setId(e.getId());
        out.setName(e.getName());
        out.setRole(e.getRole());
        out.setEmail(e.getEmail());
        return out;
    }

    public static GuestDtos.Response toResponse(Guest g) {
        GuestDtos.Response out = new GuestDtos.Response();
        out.setId(g.getId());
        out.setName(g.getName());
        out.setEmail(g.getEmail());
        if (g.getRoom() != null) {
            out.setRoomId(g.getRoom().getId());
            out.setRoomNumber(g.getRoom().getNumber());
            out.setRoomType(g.getRoom().getType());
        }
        return out;
    }
}
