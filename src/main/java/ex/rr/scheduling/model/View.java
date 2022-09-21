package ex.rr.scheduling.model;

public class View {

    public interface IAdmin extends ILocation, ISettings, IRole {

    }

    public interface ISettings {

    }

    public interface IRole {

    }

    public interface ILocation extends ISessionYear {

    }

    public interface ISessionYear extends ISessionMonth {

    }

    public interface ISessionMonth extends ISessionDay {

    }

    public interface ISessionDay extends ISession {

    }

    public interface ISession extends IUser {

    }

    public interface IUser {

    }

}
