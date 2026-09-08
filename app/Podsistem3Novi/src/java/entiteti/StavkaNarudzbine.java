/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "stavka_narudzbine")
@NamedQueries({
    @NamedQuery(name = "StavkaNarudzbine.findAll", query = "SELECT s FROM StavkaNarudzbine s")})
public class StavkaNarudzbine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdStavkaNar")
    private Integer idStavkaNar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdArt")
    private int idArt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdProdavca")
    private int idProdavca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Kolicina")
    private int kolicina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "JedinicnaCena")
    private BigDecimal jedinicnaCena;
    @JoinColumn(name = "IdNar", referencedColumnName = "IdNar")
    @ManyToOne(optional = false)
    private Narudzbina idNar;

    public StavkaNarudzbine() {
    }

    public StavkaNarudzbine(Integer idStavkaNar) {
        this.idStavkaNar = idStavkaNar;
    }

    public StavkaNarudzbine(Integer idStavkaNar, int idArt, int idProdavca, int kolicina, BigDecimal jedinicnaCena) {
        this.idStavkaNar = idStavkaNar;
        this.idArt = idArt;
        this.idProdavca = idProdavca;
        this.kolicina = kolicina;
        this.jedinicnaCena = jedinicnaCena;
    }

    public Integer getIdStavkaNar() {
        return idStavkaNar;
    }

    public void setIdStavkaNar(Integer idStavkaNar) {
        this.idStavkaNar = idStavkaNar;
    }

    public int getIdArt() {
        return idArt;
    }

    public void setIdArt(int idArt) {
        this.idArt = idArt;
    }

    public int getIdProdavca() {
        return idProdavca;
    }

    public void setIdProdavca(int idProdavca) {
        this.idProdavca = idProdavca;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getJedinicnaCena() {
        return jedinicnaCena;
    }

    public void setJedinicnaCena(BigDecimal jedinicnaCena) {
        this.jedinicnaCena = jedinicnaCena;
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
        hash += (idStavkaNar != null ? idStavkaNar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StavkaNarudzbine)) {
            return false;
        }
        StavkaNarudzbine other = (StavkaNarudzbine) object;
        if ((this.idStavkaNar == null && other.idStavkaNar != null) || (this.idStavkaNar != null && !this.idStavkaNar.equals(other.idStavkaNar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StavkaNarudzbine[ idStavkaNar=" + idStavkaNar + " ]";
    }
    
}
