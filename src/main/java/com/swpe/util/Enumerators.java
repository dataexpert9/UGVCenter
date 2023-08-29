package com.swpe.util;

import javax.swing.text.html.parser.Entity;

public class Enumerators {

    public enum Roles {
        SuperAdmin,
        User
    }
    public static Roles RoleIntToEnum(int x) {
        switch(x) {
            case 0:
                return Roles.SuperAdmin;
            case 1:
                return Roles.User;
            default:
                return null;
        }
    }

    public enum EntityTypes{
        User,
        UGV,
        MAP
    }

    public static EntityTypes EntityTypeIntToEnum(int x) {
        switch(x) {
            case 0:
                return EntityTypes.User;
            case 1:
                return EntityTypes.UGV;
            case 2:
                return EntityTypes.MAP;
            default:
                return null;
        }
    }

    //0 chhose map, 1 get path; 2 start move,3 stop move 4 man control, 5 send order,6 send InterVal
    public enum UGVMessageTypes{
        CHOOSEMAP,
        GETPATH,
        STARTMOVE,
        STOPMOVE,
        MANUALCONTROL,
        SENDORDER,
        SENDINTERVAL
    }

    public static UGVMessageTypes UGVMessageTypesIntToEnum(int x) {
        switch(x) {
            case 0:
                return UGVMessageTypes.CHOOSEMAP;
            case 1:
                return UGVMessageTypes.GETPATH;
            case 2:
                return UGVMessageTypes.STARTMOVE;
            case 3:
                return UGVMessageTypes.STOPMOVE;
            case 4:
                return UGVMessageTypes.MANUALCONTROL;
            case 5:
                return UGVMessageTypes.SENDORDER;
            case 6:
                return UGVMessageTypes.SENDINTERVAL;
            default:
                return null;
        }
    }




    public enum ActionTypes{
        USER,
        ADMIN,
        UGV,
        MAP,
        LOG,
        SOCKET
    }

    public enum SubActionTypes{
        CREATE,
        UPDATE,
        GETALL,
        GETBYID,
        DELETE,
        LOGIN,
    }





    public static ActionTypes ActionTypesIntToEnum(int x) {
        switch(x) {
            case 0:
                return ActionTypes.USER;
            case 1:
                return ActionTypes.ADMIN;
            case 2:
                return ActionTypes.UGV;
            case 3:
                return ActionTypes.MAP;
            case 4:
                return ActionTypes.LOG;
            default:
                return null;
        }
    }

    public static SubActionTypes SubActionTypesIntToEnum(int x) {
        switch(x) {
            case 0:
                return SubActionTypes.CREATE;
            case 1:
                return SubActionTypes.UPDATE;
            case 2:
                return SubActionTypes.GETALL;
            case 3:
                return SubActionTypes.GETBYID;
            case 4:
                return SubActionTypes.DELETE;
            default:
                return null;
        }
    }
}

