package fr.catcore.gamegui;

import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.devtech.grossfabrichacks.entrypoints.PrePreLaunch;
//import net.devtech.grossfabrichacks.entrypoints.PrePrePreLaunch;
//import net.devtech.grossfabrichacks.instrumentation.InstrumentationApi;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.*;

public class PrePreGameGui /*implements PrePrePreLaunch*/ {

    public void onPrePreLaunch() {
//        InstrumentationApi.pipeClassThroughTransformerBootstrap("com.mojang.serialization.codecs.RecordCodecBuilder");
    }

    public void onPrePrePreLaunch() {
        onPrePreLaunch();
//        InstrumentationApi.retransform(RecordCodecBuilder.class, (s, b) -> {
//
////            for(MethodNode node : b.methods) {
////                if("build".equals(node.name)) {
////                    System.out.println(node);
////                    node.instructions.clear(); // remove all old instructions
////                    node.visitIntInsn(ALOAD, 0);
////                    node.visitMethodInsn(INVOKESTATIC, "com/mojang/serialization/codecs/RecordCodecBuilder", "unbox", "(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;", false);
////                    node.visitIntInsn(ASTORE, 1);
////
//////                    node.visit;
////
////                    //                    node.visitInsn(ICONST_1); // load true on stack
//////                    node.visitInsn(IRETURN); // return true
////                }
////            }
//        });
    }
}
