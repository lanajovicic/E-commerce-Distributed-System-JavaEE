/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "transakcija")
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdTrans")
    private Integer idTrans;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Suma")
    private BigDecimal suma;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VremePlacanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremePlacanja;
    @JoinColumn(name = "IdNar", referencedColumnName = "IdNar")
    @ManyToOne(optional = false)
    private Narudzbina idNar;

    public Transakcija() {
    }

    public Transakcija(Integer idTrans) {
        this.idTrans = idTrans;
    }

    public Transakcija(Integer idTrans, BigDecimal suma, Date vremePlacanja) {
        this.idTrans = idTrans;
        this.suma = suma;
        this.vremePlacanja = vremePlacanja;
    }

    public Integer getIdTrans() {
        return idTrans;
    }

    public void setIdTrans(Integer idTrans) {
        this.idTrans = idTrans;
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public void setSuma(BigDecimal suma) {
        this.suma = suma;
    }

    public Date getVremePlacanja() {
        return vremePlacanja;
    }

    public void setVremePlacanja(Date vremePlacanja) {
        this.vremePlacanja = vremePlacanja;
    }

    public Narudzbina getIdNar() {
        return idNar;
    }

    public void setIdNar(Narudzbina idNar) {
        this.idNar = idNar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTrans != null ? idTrans.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTrans == null && other.idTrans != null) || (this.idTrans != null && !this.idTrans.equals(other.idTrans))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ idTrans=" + idTrans + " ]";
    }
    
}
