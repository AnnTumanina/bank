package com.company;

import java.util.Scanner;


class Account extends Thread{
    private int money = 0;
    private int maxMoney = 1500;
    int minus = 0;
    public synchronized void setBalance(){
        while(money >= maxMoney){
            try{
                wait();
            }
            catch (Exception ex){
                ex.getMessage();
            }
        }
        money += 100;
        System.out.println("Пополнение баланса на 100 \nОстаток баланcа: " + money);
        notify();
    }
    public synchronized void getMoney(){
        while(money < maxMoney){
            try {
                wait();
            }
            catch (Exception ex){
                ex.getMessage();
            }
        }
        System.out.println("Введите сумму вывода: ");
        Scanner in = new Scanner(System.in);
        minus = in.nextInt();
        money -= minus;
        if(money < 0){
            System.out.println("На балансе нет такой суммы");
            System.exit(0);
        }
        System.out.println("Произошло списание в размере \n" + minus + "\nОстаток баланса: " + money);
        notify();
    }
}

class SetBalance extends Thread{
    Account account;
    SetBalance(Account account){
        this.account=account;
    }
    public void run(){
        for (int i = 1; i < 50; i++) {
            account.setBalance();
        }
    }
}
class GetMoney extends Thread{

    Account account;
    GetMoney(Account account){
        this.account=account;
    }
    public void run(){
        for (int i = 1; i < 50; i++) {
            account.getMoney();
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        GetMoney setBal = new GetMoney(account);
        SetBalance getMon = new SetBalance(account);
        new Thread(setBal).start();
        new Thread(getMon).start();
    }
}


/*
class Main {
    public static void main(String[] args) {
        Account account = new Account();
        Thread refillThread = new Thread(){
            @Override
            public void run() {
                while (true) { //бесконечный цикл
                    try {
                        account.refillProcess();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        Thread checkoutProcess = new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        account.checkoutProcess();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        refillThread.start();
        checkoutProcess.start();
    }

    public static class Account {

        private static final int MAX_BALANCE = 3000;
        private static final int REFILL_MONEY = 300;

        private int balance;

        public synchronized void refillProcess() throws InterruptedException { //пополнить
            if (balance >= MAX_BALANCE) wait(); //останавливаем рефиллпоток
            balance += REFILL_MONEY; //но если не останавливаем, то пополняем сумму
            System.out.printf("Refill %d₽.\n", REFILL_MONEY);
            System.out.println((char) 27 + "[37mBalance: " + balance + "₽." + (char) 27 + "[0m");
            System.out.println();
            if (balance >= MAX_BALANCE) {
                notify(); //будит процесс, снимающий деньги
                return;
            }
            Thread.sleep(1000L); //задержка между пополнениями
        }

        public synchronized void checkoutProcess() throws InterruptedException { //снять
            if (balance < MAX_BALANCE) wait(); //ждём, пока баланс не будет больше или равен максимальному
            System.out.print((char) 27 + "[45mEnter the withdrawal sum: " + (char)27 + "[0m"); //если больше или равен, просим пользователя ввести снимаемую сумму
            Scanner scanner = new Scanner(System.in);
            int checkoutSum = scanner.nextInt();
            if (balance < checkoutSum) checkoutSum = balance; //если баланс меньше снимаемой суммы, то снимая сумма это весь баланс
            balance -= checkoutSum; //из баланса вычитаем снимаемую сумму (это в любом случае)
            System.out.println();
            System.out.println("Withdrawal " + checkoutSum + "₽.");
            System.out.println((char) 27 + "[37mBalance: " + balance + "₽." + (char)27 + "[0m");
            System.out.println();
            if (balance < MAX_BALANCE) { //если баланс меньше максимального
                Thread.sleep(3000L); //задержка после снятия
                notify(); //будит пополняющий баланс процесс
            }
        }

    }

}
*/