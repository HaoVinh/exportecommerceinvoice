package lixco.com.einvoice_service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.einvoice_entity.ConfigEInvoice;
import lixco.com.einvoice_entity.EInvoiceData;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EInvoiceService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;
	public int updateConfigEInvocie(ConfigEInvoice hddt){
		int res=-1;
		try{
			if(em.merge(hddt)!=null){
				res=0;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public int insertConfigEInvoice(ConfigEInvoice hddt){
		int res=-1;
		try{
			em.persist(hddt);
			if(hddt.getId()!=0){
				res=0;
			}
		}catch (Exception e) {
		}
		return res;
	}
	public ConfigEInvoice getConfigEInvoice(){
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ConfigEInvoice> cq = cb.createQuery(ConfigEInvoice.class);
			Root<ConfigEInvoice> root = cq.from(ConfigEInvoice.class);
			cq.select(root);
			TypedQuery<ConfigEInvoice> query = em.createQuery(cq);
			List<ConfigEInvoice> list =  query.getResultList();
			if(list.size()>0){
				return list.get(0);
			}
		}catch (Exception e) {
		}
		return null;
	}
	public ConfigEInvoice selectConfigEInvoiceById(long id){
		try{
			return em.find(ConfigEInvoice.class, id);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public int updateEInvoiceData(EInvoiceData data){
		int res=-1;
		try{
			if(em.merge(data)!=null){
				res=0;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public int insertEInvoiceData(EInvoiceData data){
		int res=-1;
		try{
			em.persist(data);
			if(data.getId() !=0){
				res=0;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public EInvoiceData selectEInvoiceDataById(long id){
		try{
			return em.find(EInvoiceData.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
