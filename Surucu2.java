public class Surucu2 {
    public static void main(String[] args) {
        Scheduler programlayici = new Scheduler();

        programlayici.setResourcesCount(3);
//        programlayici.setResourceCount(2);
        job j0 = new job(0,0,"H",3);
        job j1 = new job(0,1,"H",2);
        job j2 = new job(2,2,"M",3);
        job j3 = new job(3,3,"H",3);
        job j7 = new job(1,7,"L",3);
        job j8 = new job(7,8,"L",3);
//        job j9 = new job(5,9,"M",5);
//        programlayici.add(j9);

//        programlayici.add(j7);
//        programlayici.add(j8);

        ////1

        programlayici.add(j0);
        programlayici.add(j1);
        programlayici.add(j2);
        programlayici.add(j3);
        programlayici.run();
        System.out.println("-------");
        System.out.println("utilization");
//        programlayici.utilization(1);
//        programlayici.utilization(2);

        System.out.println("-------");
        System.out.println("resorce explorer");
        programlayici.resourceExplorer(1);
        programlayici.resourceExplorer(2);

        System.out.println("-------");
        System.out.println("job explorer");
        programlayici.jobExplorer(j1);
        programlayici.jobExplorer(j2);
        programlayici.jobExplorer(j3);
        programlayici.jobExplorer(j0);
//        programlayici.jobExplorer(j8);



    }
}
