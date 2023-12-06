package cn.telecom.server;

public interface Filter<Pkg> {
    boolean filter(Session session, Pkg pkg);
}
