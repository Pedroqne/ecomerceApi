package auth_service.enums;

public enum Values{
    ADMIN(1L),
    BASIC(2L);

    long roleId;

    Values(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }
}
