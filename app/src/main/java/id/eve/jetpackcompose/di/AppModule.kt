package id.eve.jetpackcompose.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.eve.jetpackcompose.data.NoteRepositoryImpl
import id.eve.jetpackcompose.domain.repository.NoteRepository
import id.eve.jetpackcompose.domain.usecase.NoteInteractor
import id.eve.jetpackcompose.domain.usecase.NoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): CollectionReference {
        val db = FirebaseFirestore.getInstance()
        return db.collection("Notes")
    }

    @Provides
    @Singleton
    fun provideCourseRepository(db: CollectionReference): NoteRepository {
        return NoteRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideAddCourseUseCase(noteRepository: NoteRepository): NoteUseCase {
        return NoteInteractor(noteRepository)
    }
}