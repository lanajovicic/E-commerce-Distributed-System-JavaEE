/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User
 */
@Entity
@Table(name = "narudzbina")
@NamedQueries({
    @NamedQuery(name = "Narudzbina.findAll", query = "SELECT n FROM Narudzbina n")})
public class Narudzbina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdNar")
    private Integer idNar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKupca")
    private int idKupca;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "UkupnaCena")
    private BigDecimal ukupnaCena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VremeNarudzbine")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vremeNarudzbine;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "AdresaDostave")
    private String adresaDostave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "GradDostave")
    private String gradDostave;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNar")
    private Collection<StavkaNarudzbine> stavkaNarudzbineCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNar")
    private Collection<Transakcija> transakcijaCollection;

    public Narudzbina() {
    }

    public Narudzbina(Integer idNar) {
        this.idNar = idNar;
    }

    public Narudzbina(Integer idNar, int idKupca, BigDecimal ukupnaCena, Date vremeNarudzbine, String adresaDostave, String gradDostave) {
        this.idNar = idNar;
        this.idKupca = idKupca;
        this.ukupnaCena = ukupnaCena;
        this.vremeNarudzbine = vremeNarudzbine;
        this.adresaDostave = adresaDostave;
        this.gradDostave = gradDostave;
    }

    public Integer getIdNar() {
        return idNar;
    }

    public void setIdNar(Integer idNar) {
        this.idNar = idNar;
    }

    public int getIdKupca() {
        return idKupca;
    }

    public void setIdKupca(int idKupca) {
        this.idKupca = idKupca;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public Date getVremeNarudzbine() {
        return vremeNarudzbine;
    }

    public void setVremeNarudzbine(Date vremeNarudzbine) {
        this.vremeNarudzbine = vremeNarudzbine;
    }

    public String getAdresaDostave() {
        return adresaDostave;
    }

    public void setAdresaDostave(String adresaDostave) {
        this.adresaDostave = adresaDostave;
    }

    public String getGradDostave() {
        return gradDostave;
    }

    public void setGradDostave(String gradDostave) {
        this.gradDostave = gradDostave;
    }

    public Collection<StavkaNarudzbine> getStavkaNarudzbineCollection() {
        return stavkaNarudzbineCollection;
    }

    public void setStavkaNarudzbineCollection(Collection<StavkaNarudzbine> stavkaNarudzbineCollection) {
        this.stavkaNarudzbineCollection = stavkaNarudzbineCollection;
    }

    public Collection<Transakcija> getTransakcijaCollection() {
        return transakcijaCollection;
    }

    public void setTransakcijaCollection(Collection<Transakcija> transakcijaCollection) {
        this.transakcijaCollection = transakcijaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNar != null ? idNar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Narudzbina)) {
            return false;
        }
        Narudzbina other = (Narudzbina) object;
        if ((this.idNar == null && other.idNar != null) || (this.idNar != null && !this.idNar.equals(other.idNar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Narudzbina[ idNar=" + idNar + " ]";
    }
    
}
