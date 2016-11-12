package factory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import AC.Autowired;
import AC.Component;
import bean.Bean;
import bean.Property;



@SuppressWarnings("unchecked")
public class ClassPathXmlApplicationContext {

	Element Ele;

	public ClassPathXmlApplicationContext(String xmlFile) throws Exception {

		// TODO Auto-generated constructor stub
		SAXReader reader = new SAXReader();
		//读取XML
		File file = new File("src/" + xmlFile);
		Document document = reader.read(file);
		Ele = document.getRootElement();
		//Component注解
		Component();
		//含有AutoWired注解
		ref_Autowired();
	}
	
	Map<String, Bean>  Ubeans = new HashMap<>();
	Map<String, Object> Beans = new HashMap<>();

	private void ref_Autowired() throws Exception {
		//解析XML
		List<Element> ele = Ele.elements();
		for (Element ce : ele) {
			Bean bean = new Bean();
			for (Attribute a : (List<Attribute>) ce.attributes()) {
				if ("id".equals(a.getName())) {
					bean.setId(a.getValue());
				} else if ("class".equals(a.getName())) {
					bean.setClassName(a.getValue());
				}
			}
			List<Property> properties = new ArrayList<>();
			for (Element e : (List<Element>) ce.elements()) {
				Property property = new Property();
				for (Attribute a : (List<Attribute>) e.attributes()) {
					if ("name".equals(a.getName())) {
						property.setName(a.getValue());
					} else if ("ref".equals(a.getName())) {
						property.setRef(a.getValue());
					} else if ("value".equals(a.getName())) {
						property.setValue(a.getValue());
					}

				}
				properties.add(property);
			}
			bean.setProperties(properties);
			Ubeans.put(bean.getId(), bean);
		}
		
		while (!Ubeans.isEmpty()) {
			for(Bean id:Ubeans.values()){
				boolean flag = true;
				for(Property p: Ubeans.get(id.getId()).getProperties()){
					if(p.getRef()==null){
						
					}else if(Beans.get(p.getRef())==null){
						flag = false;
					}
				}
				if(flag==true){
					Class c = null;
					Object o = null;
					try{
					c = Class.forName(id.getClassName());
					o = c.newInstance();
					for(Property p: Ubeans.get(id.getId()).getProperties()){
						if(p.getRef()==null){
							Field[] fields = c.getDeclaredFields();
							for(Field field:fields){
								field.setAccessible(true);
								if(field.getName().equals(p.getName())){
									System.out.println(p.getValue());
									field.set(o,p.getValue());
								}
							}
						}else{
							Field[] fields = c.getDeclaredFields();
							for(Field field:fields){
								if(field.getName().equals(p.getName())){
									field.setAccessible(true);
									field.set(c,Beans.get(p.getRef()));
								}
							}
						}
					}
					}catch (InstantiationException e) {
						//若无法实例则用Autowired
						// TODO: handle exception
						Constructor[] cs = c.getConstructors();
						for(Constructor constructor:cs){
							if(constructor.isAnnotationPresent(Autowired.class)){
								int parmaNum = constructor.getParameterTypes().length;
								Object[] parmas = new Object[parmaNum];
								for(int j=0;j<parmaNum;j++){
									for(Property p: Ubeans.get(id.getId()).getProperties()){
										if(Beans.get(p.getRef()).getClass().equals(constructor.getParameterTypes()[j])){
											//System.out.println(ini);
											parmas[j] = Beans.get(p.getRef());
										}
									}
								}
								o = constructor.newInstance(parmas);
							}
						}
						
					}
					Ubeans.remove(id.getId());
					Beans.put(id.getId(), o);
				}
			}
		}
		
	}
	//将test中的.java遍历一遍
	private void Component() throws Exception{
		File basePackge = new File(System.getProperty("user.dir")+"//src//test");
		for(File classFile : basePackge.listFiles()){
			String className = classFile.getName().substring(0,classFile.getName().length()-5);
			Class c = Class.forName("test."+className);
			if(c.isAnnotationPresent(Component.class)){
				Component com = (Component)c.getAnnotation(Component.class);
				Beans.put(com.value(), c.newInstance());
			}
		}
		
	}

	public Object getBean(String id){
		return Beans.get(id);
	}
}
