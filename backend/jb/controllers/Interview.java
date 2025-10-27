package com.jb.jb.controllers;

public class Interview {
    String firstName="Jeevanandham";
    char lastName='R';
    int age=20;
    boolean bca=true;

    void display(){
        System.out.println("Name: "+firstName+" "+lastName);

        System.out.println("Age: "+age);
        System.out.println("Bca Completed: "+bca);

    }


    public static void main (String args[]){
        Interview obj=new Interview();
        obj.display();
    }
}
