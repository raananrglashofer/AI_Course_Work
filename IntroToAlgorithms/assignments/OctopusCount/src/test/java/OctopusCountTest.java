import edu.yu.introtoalgs.OctopusCountI;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;
import edu.yu.introtoalgs.OctopusCount;
import edu.yu.introtoalgs.OctopusCountI;
public class OctopusCountTest {
    @Test
    public void SameOctopusTwiceTest(){
        OctopusCount oc = new OctopusCount();
        OctopusCountI.ArmColor[] colors = {OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED,
                OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED,
                OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED};
        int[] lengthInCM = {1, 2, 3, 4, 5, 6, 7, 8};
        OctopusCountI.ArmTexture[] textures = {OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,
                OctopusCountI.ArmTexture.STICKY, OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,
                OctopusCountI.ArmTexture.STICKY, OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,};
        for(int i = 0; i < 2; i++){
            oc.addObservation(i + 1, colors, lengthInCM, textures);
        }
        Assert.assertEquals(oc.countThem(), 1);
    }
    // Create two unique Octopus and see if they are both stored in the Set
    @Test
    public void AddTwoUniqueOctopus(){
        OctopusCount oc = new OctopusCount();
        OctopusCountI.ArmColor[] colors = {OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED,
                OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED,
                OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED};
        int[] lengthInCM = {1, 2, 3, 4, 5, 6, 7, 8};
        OctopusCountI.ArmTexture[] textures = {OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,
                OctopusCountI.ArmTexture.STICKY, OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,
                OctopusCountI.ArmTexture.STICKY, OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,};
        oc.addObservation(1, colors, lengthInCM, textures);
        OctopusCountI.ArmColor[] colors2 = {OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED,
                OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.RED,
                OctopusCountI.ArmColor.BLACK, OctopusCountI.ArmColor.GRAY, OctopusCountI.ArmColor.RED};
        int[] lengthInCM2 = {1, 4, 3, 9, 5, 6, 5, 8};
        OctopusCountI.ArmTexture[] textures2 = {OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,
                OctopusCountI.ArmTexture.STICKY, OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,
                OctopusCountI.ArmTexture.SMOOTH, OctopusCountI.ArmTexture.SLIMY, OctopusCountI.ArmTexture.SMOOTH,};
        oc.addObservation(2, colors2, lengthInCM2, textures2);
        Assert.assertEquals(oc.countThem(), 2);
    }
}

//    @Test(enabled=true, timeOut=500)
//    public void trivialAreIdentical() {
//        final OctopusCountI oc = new OctopusCount();
//        final OctopusCountI.ArmColor [] colors1 = {OctopusCountI.ArmColor.GRAY , OctopusCountI.ArmColor.GRAY , OctopusCountI.ArmColor.GRAY , OctopusCountI.ArmColor.RED , OctopusCountI.ArmColor.RED , OctopusCountI.ArmColor.RED , OctopusCountI.ArmColor.BLACK , OctopusCountI.ArmColor.BLACK};
//        final int [] lengthInCM1 = {1 , 2 , 3 , 4 , 5 , 6 , 7 , 8};
//        final OctopusCountI.ArmTexture[] textures1 = {
//                OctopusCountI.ArmTexture.SMOOTH, OctopusCountI.ArmTexture.SMOOTH, OctopusCountI.ArmTexture.SMOOTH, OctopusCountI.ArmTexture.SLIMY , OctopusCountI.ArmTexture.SLIMY , OctopusCountI.ArmTexture.SLIMY , OctopusCountI.ArmTexture.STICKY , OctopusCountI.ArmTexture.STICKY};
//        addObservation ( oc , colors1 , lengthInCM1 , textures1 ) ;
//        addObservation ( oc , colors1 , lengthInCM1 , textures1 ) ;
//        final int count = oc.countThem() ;
//        Assert.assertEquals(count , 1 , "the two observation are trivially identical! " ) ;
//    }
//    private static void addObservation ( final OctopusCountI oc ,
//                                         final OctopusCountI.ArmColor [] colors ,
//                                         final int [ ] lengthInCM ,
//                                         final OctopusCountI.ArmTexture [] textures )
//    {
//        logger.info ( "colors = {}" , Arrays.toString ( colors ) ) ;
//        logger.info ( "lengthInCM = {}" , Arrays.toString ( lengthInCM ) ) ;
//        logger.info ( "textures = {}" , Arrays.toString ( textures ) ) ;
//        try {
//            oc.addObservation(observationCounter++ , colors , lengthInCM ,
//                    textures ) ;
//        }
//        catch ( IllegalArgumentException iae ) {
//            throw iae ; // probably expected , whatever
//        }
//        catch ( Exception e ) {
//            final String msg = "Unexpected exception" ;
//            logger.error (msg , e ) ;
//            throw new RuntimeException (msg , e ) ;
//        }
//    }
//}
