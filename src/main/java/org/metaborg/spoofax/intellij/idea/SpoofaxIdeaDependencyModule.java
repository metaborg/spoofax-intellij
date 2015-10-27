package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.SdkType;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.factories.*;
import org.metaborg.spoofax.intellij.idea.languages.*;
import org.metaborg.spoofax.intellij.project.*;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleBuilder;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleType;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplEditor;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplPanel;
import org.metaborg.spoofax.intellij.idea.project.LanguageImplTableModel;
import org.metaborg.spoofax.intellij.menu.*;
import org.metaborg.spoofax.intellij.sdk.SpoofaxSdkType;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * The Guice dependency injection module for the Spoofax IDEA plugin.
 */
public final class SpoofaxIdeaDependencyModule extends SpoofaxIntelliJDependencyModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bind(IIdeaAttachmentManager.class).to(IdeaAttachmentManager.class).in(Singleton.class);
        bind(IIdeaLanguageManager.class).to(IdeaLanguageManagerImpl.class).in(Singleton.class);
        bind(SpoofaxModuleBuilder.class).in(Singleton.class);
        bind(ILexerParserManager.class).to(LexerParserManager.class).in(Singleton.class);
        bind(BuilderMenuBuilder.class).in(Singleton.class);
        bind(ActionHelper.class).in(Singleton.class);
        bind(StrategoResourceTransformer.class).in(Singleton.class);
        bind(IResourceTransformer.class).to(StrategoResourceTransformer.class);
        bind(new TypeLiteral<ResourceTransformer<?, ?, ?>>() {}).to(StrategoResourceTransformer.class);
        bind(new TypeLiteral<ResourceTransformer<IStrategoTerm, IStrategoTerm, IStrategoTerm>>() {}).to(
                StrategoResourceTransformer.class);

        bind(SpoofaxSyntaxHighlighterFactory.class);
        bind(IParserConfiguration.class).toInstance(new JSGLRParserConfiguration(
            /* implode    */ true,
            /* recovery   */ true,
            /* completion */ false,
            /* timeout    */ 30000));

        install(new FactoryModuleBuilder()
                        .implement(Lexer.class, SpoofaxLexer.class)
                        .build(IHighlightingLexerFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(Lexer.class, CharacterLexer.class)
                        .build(ICharacterLexerFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(ParserDefinition.class, SpoofaxParserDefinition.class)
                        .build(IParserDefinitionFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(IntelliJProject.class, IntelliJProject.class)
                        .build(IProjectFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(LanguageImplTableModel.class, LanguageImplTableModel.class)
                        .build(ILanguageImplTableModelFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(LanguageImplPanel.class, LanguageImplPanel.class)
                        .build(ILanguageImplPanelFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(LanguageImplEditor.class, LanguageImplEditor.class)
                        .build(ILanguageImplEditorFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(TransformationAction.class, TransformationAction.class)
                        .build(ITransformIdeaActionFactory.class));
        install(new FactoryModuleBuilder()
                        .implement(BuilderActionGroup.class, BuilderActionGroup.class)
                        .build(IBuilderActionGroupFactory.class));
        install(new IntelliJExtensionProviderFactory().provide(SpoofaxSdkType.class, SdkType.EP_NAME.getName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindProject() {
        bind(IntelliJProjectService.class).in(Singleton.class);
        bind(IProjectService.class).to(CompoundProjectService.class).in(Singleton.class);
//        bind(IProjectService.class).to(IntelliJProjectService.class).in(Singleton.class);
        bind(IIntelliJProjectService.class).to(IntelliJProjectService.class).in(Singleton.class);
        bind(ArtifactProjectService.class).in(Singleton.class);
        bind(CompoundProjectService.class).in(Singleton.class);

        Multibinder<IProjectService> uriBinder = Multibinder.newSetBinder(binder(), IProjectService.class, Compound.class);
        uriBinder.addBinding().to(IntelliJProjectService.class);
        uriBinder.addBinding().to(ArtifactProjectService.class);
    }

    @Provides
    @Singleton
    private SpoofaxModuleType provideModuleType() {
        return (SpoofaxModuleType) ModuleTypeManager.getInstance().findByID(SpoofaxModuleType.ID);
    }
}
