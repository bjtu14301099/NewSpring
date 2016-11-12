package test;

import factory.ClassPathXmlApplicationContext;


public class test {

    public static void main(String[] args) throws Exception {
        String locations = "bean.xml";
        ClassPathXmlApplicationContext ctx = 
		    new ClassPathXmlApplicationContext(locations);
        boss boss = (boss) ctx.getBean("boss");
        System.out.println(boss.tostring());
    }
}