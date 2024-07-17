public class Surucu3 {
    public static void main(String[] args) {
    Scheduler programlayici = new Scheduler();
//    programlayici.setResourceCount(3);
    programlayici.setResourcesCount(3);

    job j0 = new job(1,0,"H",3);
    job j1 = new job(1,1,"H",2);
    job j2 = new job(1,2,"M",3);
    job j3 = new job(4,3,"H",3);
    job j4 = new job(6,5,"L",2);
    job j6 = new job(1,6,"L",1);
    job j7 = new job(6,7,"L",1);
        programlayici.add(j7);
        programlayici.add(j4);
        programlayici.add(j6);
    programlayici.add(j0);
    programlayici.add(j1);
    programlayici.add(j2);
    programlayici.add(j3);
    programlayici.run();
    System.out.println("-------");
    System.out.println("utilization");
    programlayici.utilization(1);
    programlayici.utilization(2);
//    programlayici.utilization(3);
    System.out.println("-------");
        System.out.println("resorce explorer");
        programlayici.resourceExplorer(1);
    programlayici.resourceExplorer(2);
//        programlayici.resourceExplorer(3);
    System.out.println("-------");
    System.out.println("job explorer");
        programlayici.jobExplorer(j1);
        programlayici.jobExplorer(j0);

        programlayici.jobExplorer(j6);
        programlayici.jobExplorer(j4);
        programlayici.jobExplorer(j2);
        programlayici.jobExplorer(j7);
        programlayici.jobExplorer(j3);

    programlayici.jobExplorer(j0);



    }
}
