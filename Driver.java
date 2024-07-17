public class Driver {
    public static void main(String[] args) {

        job j0 = new job(0,0,"H",3);
        job j1 = new job(0,1,"H",2);
        job j2 = new job(5,2,"M",3);
        job j3 = new job(10,3,"H",3);

        Scheduler programlayici = new Scheduler();
        programlayici.setResourcesCount(2);
        programlayici.add(j0);
        programlayici.add(j1);
        programlayici.add(j2);
        programlayici.add(j3);
        programlayici.run();
        System.out.println("-------");
        programlayici.utilization(1); // kaynak 1, toplam 6 birim çalışmış ve arada boş kaldığı zaman              // olmamış, 6/6 = 1
        System.out.println("-------");
        programlayici.resourceExplorer(2);
        System.out.println("-------");
        programlayici.jobExplorer(j3);

    }
    /*
    public static void main(String[] args) {
        job j0 = new job(3,0,"H",3);
        job j1 = new job(1,1,"H",2);
        job j2 = new job(0,2,"M",3);
        job j3 = new job(4,3,"H",3);

        Scheduler programlayici = new Scheduler();
        programlayici.setResourcesCount(3);
        programlayici.add(j0);
        programlayici.add(j1);
        programlayici.add(j2);
        programlayici.add(j3);

            programlayici.run();
            System.out.println("-util1 :------");
            programlayici.utilization(1); // kaynak 1, toplam 6 birim çalışmış ve arada boş kaldığı zaman              // olmamış, 6/6 = 1
             System.out.println("-util2 :------");
            programlayici.utilization(2); // kaynak 1, toplam 6 birim çalışmış ve arada boş kaldığı zaman              // olmamış, 6/6 = 1
            System.out.println("-util3 :------");
            programlayici.utilization(3); // kaynak 1, toplam 6 birim çalışmış ve arada boş kaldığı zaman              // olmamış, 6/6 = 1
            System.out.println("-resourceExp : 1------");
            programlayici.resourceExplorer(1);
            System.out.println("-resourceExp : 2------");
            programlayici.resourceExplorer(2);
            System.out.println("-resourceExp : 3------");
            programlayici.resourceExplorer(3);
            System.out.println("--jobs : -----");
            programlayici.jobExplorer(j0);
            programlayici.jobExplorer(j1);
            programlayici.jobExplorer(j2);
            programlayici.jobExplorer(j3);


    }*/
}
