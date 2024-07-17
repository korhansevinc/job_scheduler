/*
* Job-Resource Scheduler
* Son Degistirilme : 30/10/2022
* */
public class Scheduler <E> {
    /*kullanilacak kaynak(islemci) sayisi */
    private int numberOfProcessor ;
    private int totalCalismaSuresi = 0 ;
    private ArrayList<Processor> processorArray ;

    /*waitList : arrivalTime'a gore siralanmis joblar  */
    private SinglyLinkedList waitList = new SinglyLinkedList();

    /*highPQueue, midPQueue,lowPQueue oncelikli siralar*/
    private Queue highPQueue = new Queue();
    private Queue midPQueue = new Queue();
    private Queue lowPQueue = new Queue();

    public Scheduler(){}


    /*
     * Scheduling'de kullanilacak toplam islemci sayisini ayarlayan metod.
     */
    public void setResourcesCount(int processorNumber){
        numberOfProcessor = processorNumber ;
        processorArray = new ArrayList<Processor>(numberOfProcessor);
        for(int i = 0 ; i< processorNumber;i++){
            processorArray.add(new Processor(i+1));
        }
    }
    /*
     * Her yeni gelen islemi waitList'e ekleyen ve gelen job'ların arrivalTime'larina gore siralayan metod.
     */
    public void add(job newJob){
        if(waitList.isEmpty())
            waitList.addFirst(newJob);
        else
            waitList.addLast(newJob);
        /*
         * sorting kısmı : her dongude arrivalTime'ı en kucuk olan o anki en kucuk indexe yerlestiriyoruz.
         * yontem olarak selection sort kullanildi.
         */

        Node pointer1 = waitList.head;
        Node pointer2 = pointer1.getNext();
        Node prioNode = pointer1;

        while(pointer1 != null){
            prioNode = pointer1 ;
            while(pointer2 != null){
                if(pointer2.getJob().arrivalTime < prioNode.getJob().arrivalTime){
                    prioNode = pointer2 ;
                }
                pointer2 = pointer2.getNext();
            }
            job temp = pointer1.getJob();
            pointer1.setJob(prioNode.getJob());
            prioNode.setJob(temp);

            if(pointer1.getNext() == null)
                break ;

            pointer1 = pointer1.getNext();
            pointer2 = pointer1.getNext();
        }
    }
    /*
     * istenilen islemcinin (kaynagin) verimliligini yazdiran metod.
     */
    public void utilization(int processorId){
        System.out.println("R"+processorId +" verim: "
                + (double)(processorArray.get(processorId-1).calismaSuresi)/(double)totalCalismaSuresi);
    }

    /*
     * istenilen isin (job'un) bilgilerini yazdiran metod.
     * her is yapilirken bilgileri ,yapildigi ilgili islemcinin icine yazildi.(run metodunda)
     * jobsAndStart'ta tutulan isler ve baslangic zamani bilgilerini , ve
     * job'un icinde tutulan bilgiler isiginda jobStart(baslangic),jobEnd(bitis),
     * ve baslangic - arrivalTime ile gecikme suresi bulundu ve yazdirildi.
     */
    public void resourceExplorer(int processorId){
        System.out.print("R"+(processorId) + "\t");
        String tmp = "";
        for(int i = 0 ; i<processorArray.get(processorId-1).jobsAndStart.size();i++){
            String str = processorArray.get(processorId-1).jobsAndStart.get(i);
            String id = "";
            int jobStart = 0;
            int jobEnd = 0 ;

            for(int j = 0 ; j<str.length();j++){
                if(str.charAt(j) == ','){
                    id = str.substring(0,j);
                    jobStart = Integer.parseInt(str.substring(j+1));
                }
            }
            jobEnd = jobStart + processorArray.get(processorId-1).kaynakIslemler.get(i).duration - 1 ;
            tmp = tmp + "("+id + "," + jobEnd + "," + (jobStart-processorArray.get(processorId-1).kaynakIslemler.get(i).arrivalTime) + ")," ;

            i = i + processorArray.get(processorId-1).kaynakIslemler.get(i).duration - 1 ;
        }
        if(tmp != "")
            tmp = tmp.substring(0,tmp.length()-1);
        System.out.print(tmp);
        System.out.println();
    }
    /* istenilen isin(job'un) bilgilerini yazdiran metod.
     * her is yapilirken bilgileri ,yapildigi ilgili islemcinin icine yazildi.(run metodunda)
     * job'un icindeki veriler ve ilgili islemcinin tuttugu kaynakIslemler listesini kullanarak
     * id , baslangic , bitis , gecikme yazdirildi.
     */
    public void jobExplorer(job aJob){
        System.out.println("islemno\tkaynak\tbaslangic\tbitis\tgecikme");
        System.out.print(aJob.id + "      \t");
        String kaynak = "";
        for(int i = 0 ; i<numberOfProcessor;i++){
            for(int j = 0 ; j<processorArray.get(i).kaynakIslemler.size();j++){
                if(processorArray.get(i).kaynakIslemler.get(j).id == aJob.id){

                    kaynak = "R"+(i+1);
                }
            }
        }
        // ilgili islemciyi buldurtan boolean. islemciyi bulunca donguyu gereksiz islem olmamasi icin break'letiyoruz.
        boolean hasFound = false ;
        System.out.print(kaynak + "    \t");
        String id = "";
        int jobStart = 0;
        int jobEnd = 0 ;
        int gecikme = 0 ;

        for(int i = 0 ; i<numberOfProcessor;i++){
            for(int j = 0 ; j<processorArray.get(i).jobsAndStart.size();j++){
                String tmp=processorArray.get(i).jobsAndStart.get(j);

                for(int k = 0 ; k<tmp.length();k++){
                    if(tmp.charAt(k) == ','){
                        id = tmp.substring(0,k);
                        if(Integer.parseInt(id) == aJob.id){
                            jobStart = Integer.parseInt(tmp.substring(k+1));
                            jobEnd = jobStart + processorArray.get(i).kaynakIslemler.get(j).duration - 1 ;
                            gecikme = jobStart-processorArray.get(i).kaynakIslemler.get(j).arrivalTime ;
                            hasFound = true ;
                        }
                    }
                }
                if(hasFound)
                    break ;
            }
            if(hasFound)
                break ;
        }
        System.out.print(jobStart + "        \t");
        System.out.print(jobEnd + "    \t");
        System.out.print(gecikme);
        System.out.println();
    }

    /* Cizelgelemenin yapildigi metod.
     * WaitList'teki islemler priority'lerine gore highP,midP,lowP Queue'larina dagilirlar.
     * Daha sonra arrivalTime'i gelenler o an bos olan islemcilere atanirlar.(ilk atama en kucuk id'li islemciye olmak uzere)
     * Ve islemler ekrana yazdirilir.
     */
    public void run(){
        Node position ;
        for(int i = 0 ; i<i+1;i++){
            position = waitList.head;
            while(position != null){
                if(position.getJob().arrivalTime == i){
                    switch (position.getJob().priority) {
                        case "H":
                            highPQueue.enqueue(position.getJob());
                            break;
                        case "M":
                            midPQueue.enqueue(position.getJob());
                            break ;
                        case "L":
                            lowPQueue.enqueue(position.getJob());
                            break;
                        default:
                            System.out.println("run metod default case !");
                            break ;
                    }
                }
                position = position.getNext();
            }
            boolean devamEtmeli = false ;
            Node temp = waitList.head ;
            while(temp != null){
                if(temp.getJob().arrivalTime > i){
                    devamEtmeli = true ;
                }
                temp = temp.getNext();
            }
            if(!devamEtmeli){
                break ;
            }
        }// for metodunun sonu ve highPQueue, midPQueue, lowPQueue ' ye islemlerimiz eklendi.

        /*   burada processorlara gerekli islemlerimizi atiyoruz .*/

        int x = 0 ;
        for(int i= 0; i<i+1;i++){

            for(int j = 0 ; j<numberOfProcessor;j++){

                if(!highPQueue.isEmpty()) {
                    if (highPQueue.first().arrivalTime <= i) {
                        try {
                            if(processorArray.get(j).kaynakIslemler.get(i) == null){
                                processorArray.get(j).addJobsHasBeenDone(highPQueue.dequeue());
                            }
                        }catch (Exception e){
                            processorArray.get(j).addJobsHasBeenDone(highPQueue.dequeue());
                        }
                    }
                }

                if(!midPQueue.isEmpty()) {
                    if (midPQueue.first().arrivalTime <= i) {
                        try {
                            if(processorArray.get(j).kaynakIslemler.get(i) == null){
                                processorArray.get(j).addJobsHasBeenDone(midPQueue.dequeue());
                            }
                        }catch (Exception e){
                            processorArray.get(j).addJobsHasBeenDone(midPQueue.dequeue());
                        }
                    }
                }
                if(!lowPQueue.isEmpty()) {
                    if (lowPQueue.first().arrivalTime <= i) {
                        try {
                            if(processorArray.get(j).kaynakIslemler.get(i) == null){
                                processorArray.get(j).addJobsHasBeenDone(lowPQueue.dequeue());
                            }
                        }catch (Exception e){
                            processorArray.get(j).addJobsHasBeenDone(lowPQueue.dequeue());
                        }
                    }
                }
            }

            if(highPQueue.isEmpty()){
                if(midPQueue.isEmpty()){
                    if(lowPQueue.isEmpty()){
                        break ;
                    }
                }
            }

        }// islemcilerimize hangi isler dusecek belirlendi. Artik arrival time'lara gore bu isleri yazdiralim.

        /* zaman simulasyonu */

        System.out.print("Zaman" + "\t");
        for(int i = 0 ; i<numberOfProcessor;i++){
            System.out.print("R"+(i+1));
            System.out.print("\t");
        }
        System.out.println();

        for(int i = 0 ; i<i+1;i++){
            boolean processorsTaskIsEmpty = true ;
            System.out.print(i + "    \t"); // sn

            for(int j = 0 ; j<numberOfProcessor;j++){
                if(!processorArray.get(j).jobsToGetDone.isEmpty()){
                    if(processorArray.get(j).jobsToGetDone.first().arrivalTime <= i){
                        int tmp = processorArray.get(j).jobsToGetDone.dequeue().id ;
                        System.out.print("J"+tmp + " \t");
                        processorArray.get(j).jobsAndStart.add(tmp +"," + i);
                    }
                }else {
                    System.out.print("\t");
                }
            }
            System.out.println();
            for(int j = 0 ; j<numberOfProcessor;j++){
                if(!processorArray.get(j).jobsToGetDone.isEmpty()){
                    processorsTaskIsEmpty = false ;
                }
            }
            if(processorsTaskIsEmpty){
                totalCalismaSuresi = i + 1  ;
                break ;
            }

        }
    }


    /* Processor (islemci) class'imiz.
     * Processor'lar isleri yapan , islem sirasina ve bazi istenilen ozelliklere gore islerimizi (job'lari)
     * saklayan objelerimizdir.
     */
    class Processor{
        private Queue jobsToGetDone = new Queue();
        private Queue jobsHasBeenDone = new Queue();

        private ArrayList<job> kaynakIslemler = new ArrayList<>(10) ;
        private ArrayList<String> jobsAndStart = new ArrayList<>(10);
        private job jobToBeDone;
        private int processorId ;
        private int calismaSuresi = 0 ;

        /* constructor metodlari */

        public Processor(){}
        public Processor(int id){
            kaynakIslemler = new ArrayList<>();
            for(int i = 0 ; i<kaynakIslemler.size();i++)
                kaynakIslemler.add(new job(0,0,"",0));
            processorId = id ;
        }
        /* Processor class'i icin toString metodu */
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder("");
            Node pos = jobsHasBeenDone.data.head;
            while(pos != null){
                sb.append("Job"+pos.job_atNode.id);
                sb.append(" , ");
                pos = pos.getNext();
            }
            String str = sb.toString();
            return str ;

        }

        /*
        * Bu metod parametre olarak verilen islem'i (job) kopyalar ,
        * yeni bir job objesi yaratir ve bu objeyi gerekli yerlere kaydeder , ekler.
        */
        private void addJobsHasBeenDone(job newJob){
            /* deep copies a job to not lose info */
            int duration = newJob.duration;
            String priority = newJob.priority;
            int id = newJob.id;
            int arrivalTime = newJob.arrivalTime;
            job j = new job(arrivalTime,id,priority,duration);
            jobsHasBeenDone.enqueue(j);
            for(int i = 0 ; i<j.duration;i++){
                calismaSuresi++;
                kaynakIslemler.add(j);
                jobsToGetDone.enqueue(j);
            }
        }
        /* getter ve setter'lar */
        public void setCalismaSuresi(int yeniSure){
            calismaSuresi = yeniSure ;
        }
        public int getCalismaSuresi(){
            return calismaSuresi;
        }
        public job getJobToBeDone() {return jobToBeDone;}

    }

    /* FIFO Based Queue ADT ' miz. */
    class Queue{
        /* Instance variable'lar */
        private SinglyLinkedList data = new SinglyLinkedList(); ;
        private int size = 0 ;

        /* Constructor */
        public Queue(){}

        /* Public Metodlar */

        public int size(){
            return size ;
        }
        public boolean isEmpty(){
            return size == 0 ;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder("");
            Node pos = data.head;
            while(pos != null){
                sb.append("Job"+pos.job_atNode.id);
                sb.append(" , ");
                pos = pos.getNext();
            }
            String str = sb.toString();
            return str ;

        }

        /* Siraya eleman ekler */
        public void enqueue(job newJob){
            if(isEmpty())
                data.addFirst(newJob);
            else
                data.addLast(newJob);
            size++;
        }

        /* Sirasi gelen elemani siradan cikartir ve onu doner .*/
        public job dequeue(){
            if(isEmpty())
                return null;
            job answer = data.removeFirst();
            size--;
            return answer;
        }

        /* Sirasi gelen elemani doner , siradan cikartmaz .*/
        public job first(){
            if(isEmpty())
                return null ;
            return data.first();
        }

    }

    /* Bagli Liste ADT' miz */
    class SinglyLinkedList{
        /* instance variable'larimiz */
        private Node head = null ;
        private Node tail = null ;
        private int size = 0 ;

        /* Constructor */
        public SinglyLinkedList(){}

        /* Public Metod'lar */
        public int size(){
            return size ;
        }

        public boolean isEmpty(){
            return size == 0 ;
        }

        /* Listedeki ilk elemani doner */
        public job first(){
            if(isEmpty())
                return null;
            return head.getJob();
        }
        /* Listedeki son elemani doner */
        public job last(){
            if(isEmpty())
                return null ;
            return tail.getJob();
        }
        /* Listenin basina eleman ekler. */
        public void addFirst(job newJob){
            Node newest = new Node(newJob,head);
            head = newest;
            if(size==0)
                tail = newest;
            size++;
        }

        /* Listenin sonuna eleman ekler. */
        public void addLast(job newJob){
            Node newest = new Node(newJob,null);
            if(isEmpty())
                head = newest;
            else
                tail.setNext(newest);
            tail = newest;
            size++;
        }

        /* Ilk elemani kaldirir. Ve onu doner */
        public job removeFirst(){
            if(isEmpty())
                return null ;
            job answer = head.getJob();
            head = head.getNext();
            size--;
            if(size==0)
                tail = null ;
            return answer;
        }

        /* Son elemani kaldirir. Ve onu doner */
        public job removeLast(){
            if(isEmpty())
                return null ;
            job answer = tail.getJob();
            Node position = head ;
            while(position != null){
                if(position.getNext().getNext() == null){
                    position.setNext(null);
                    tail = position ;
                    size--;
                }
                position = position.getNext();
            }
            if(size == 0)
                tail = null ;
            return answer;
        }

    }

    /* Node Class'imiz */
    class Node {
        /* instance variable'lar'*/
        private job job_atNode ;
        private Node next ;

        /* Constructor'lar */
        public Node(){
            this(null,null);
        }
        public Node(job newJob , Node newNext){
            job_atNode = newJob ;
            next = newNext ;
        }

        /* Getter ve Setter'lar */
        public job getJob(){
            return job_atNode;
        }
        public Node getNext(){
            return next ;
        }
        public void setJob(job newJob){
            job_atNode = newJob;
        }
        public void setNext(Node newNext){
            next = newNext;
        }

    }

    /* Gerekli Gorulen Metodlarla Generic ArrayList Class'imiz */
    class ArrayList<E> {

        /* instance variable'lar */
        private  E[] data ;
        private int size = 0;

        /* Constructor'lar*/
        public ArrayList(int initialCapacity){
            if(initialCapacity < 0)
                throw new IllegalArgumentException("Illegal capacity : " + initialCapacity);
            this.data = (E[])new Object[initialCapacity];
        }
        public ArrayList(){
            this(10);
        }


        /* Public Metod'lar */
        public int size(){
            return size;
        }
        public boolean isEmpty(){
            return size == 0 ;
        }

        /* istenilen index'teki elemani doner . */
        public E get(int i)throws IndexOutOfBoundsException{
            checkIndex(i,size);
            return data[i];
        }

        /* istenilen index'e istenilen elemani koyar . */
        public E set(int i , E e) throws IndexOutOfBoundsException{
            checkIndex(i, size);
            E temp = data[i];
            data[i] = e ;
            return temp ;
        }

        /* sona yeni eleman ekler */
        public void add(E e)throws IndexOutOfBoundsException,IllegalStateException{
            int capacity = data.length;
            boolean notFull = false ;
            for(int i = 0 ; i<data.length;i++){
                if(data[i] == null){
                    data[i] = e ;
                    notFull = true ;
                    break ;
                }
            }
            if(!notFull){
                int oldCapacity = data.length;
                Object oldData[] = data ;
                int newCapacity = (oldCapacity*3)/2 + 1 ;
                data = (E[]) new Object[newCapacity];
                for(int i = 0 ; i<oldData.length;i++){
                    data[i] = (E) oldData[i];
                }
                for(int i = 0 ; i<data.length;i++){
                    if(data[i] == null){
                        data[i] = e ;
                        break ;
                    }
                }
            }
            size++;
        }

        /* istenilen index'teki elemani kaldirir. */
        public E remove(int i )throws IndexOutOfBoundsException{
            checkIndex(i, size);
            E temp = data[i];
            for(int k =i;k<size-1;k++)
                data[k] = data[k+1];
            data[size-1] = null ;
            size--;
            return temp ;
        }

        /* alinan inputlarin dogrulunu kontrol eden metod. */
        private void checkIndex(int i, int n) {
            if(i < 0 || i>= n)
                throw new IndexOutOfBoundsException("Illegal index: " + i);
        }
    }

}
