package back.vybz.auth_user.user.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "deleted")
    private Boolean deleted = false;

    public void softDelete() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }

}