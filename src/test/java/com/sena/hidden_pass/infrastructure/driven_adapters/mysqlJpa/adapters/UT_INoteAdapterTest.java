package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.NoteDataProvider;
import com.sena.hidden_pass.PriorityDataProvider;
import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.INoteRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.utils.AESUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UT_INoteAdapterTest {

    @Mock
    private INoteRepository noteRepository;

    @Mock
    private IUserAdapter userAdapter;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPriorityRepository priorityRepository;

    @Mock
    private AESUtil aesUtil;

    @InjectMocks
    private INoteAdapter noteAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void cleanMocks(){
        reset(noteRepository, userAdapter, userRepository);
    }

    @Test
    void testGetAllNotesByUserSuccessfully() throws Exception {
        // Given
        UUID id = UUID.randomUUID();


        // When
        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(
                new UserModel(
                        UUID.randomUUID(),
                        new EmailValueObject("nicky@nicky.com"),
                        new UsernameValueObject("username"),
                        "master_password",
                        "http://image.co",
                        null,
                        Set.of(new NoteModel(
                                UUID.randomUUID(),
                                "test",
                                "description",
                                new PriorityModel(UUID.randomUUID(), PriorityNames.ALTA)
                        )),
                        null,
                        null
                )
        );

        when(this.aesUtil.decrypt(anyString())).thenReturn("test", "description");
        Set<NoteModel> notes = this.noteAdapter.getAllNotesByUser(id);

        // Then
        assertNotNull(notes);
        assertTrue(notes.stream()
                .anyMatch(noteModel -> "test".equals(noteModel.getTitle())));
        assertTrue(notes.stream()
                .anyMatch(noteModel -> "description".equals(noteModel.getDescription())));

        assertEquals(1, notes.size());
    }

    @Test
    void testGetAllNotesByUserNotFound(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userAdapter.getUserById(any(UUID.class))).thenThrow(new IllegalArgumentException("User with id " + id + " not found"));

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.noteAdapter.getAllNotesByUser(id);
        });

        assertEquals("User with id " + id + " not found", exception.getMessage());
    }

    // PasswordPlainText
    @Test
    void testGetAllNotesByUserAESException() throws Exception {
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.aesUtil.decrypt(anyString())).thenThrow(RuntimeException.class);

        // Then
        assertThrows(RuntimeException.class, () -> {
            this.noteAdapter.getAllNotesByUser(id);
        });
    }

    @Test
    void testGetNoteByIdSuccessfully(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.of(NoteDataProvider.getNoteDBO()));
        NoteModel result = this.noteAdapter.getNoteById(id);

        // Then
        assertNotNull(result);

        assertEquals("Title", result.getTitle());
        assertEquals("Description", result.getDescription());
        assertEquals(PriorityNames.CRITICA, result.getId_priority().getName());
    }

    @Test
    void testGetNoteByIdNotFound(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.noteAdapter.getNoteById(id);
        });

        assertEquals("Note with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testCreateNote() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID user_id = UUID.randomUUID();
        PriorityNames priority_name = PriorityNames.CRITICA;

        ArgumentCaptor<NoteDBO> noteFoundedCaptor = ArgumentCaptor.forClass(NoteDBO.class);

        // When
        when(this.priorityRepository.getByName(any(PriorityNames.class))).thenReturn(Optional.of(new PriorityDBO(UUID.randomUUID(), PriorityNames.CRITICA)));
        when(this.noteRepository.save(any(NoteDBO.class))).thenReturn(NoteDataProvider.getNoteDBO());
        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());


        when(this.aesUtil.encrypt(anyString())).thenReturn("titleEncripted", "descriptionEncripted");
        NoteModel result = this.noteAdapter.createNote(note, user_id, priority_name);

        // Then
        assertNotNull(result);

        verify(this.noteRepository).save(noteFoundedCaptor.capture());
        NoteDBO saved = noteFoundedCaptor.getValue();

        assertEquals(note.getId_note(), saved.getId_note());
        assertEquals(note.getId_priority().getName(), saved.getId_priority().getName());
        assertEquals("titleEncripted", saved.getTitle());
        assertEquals("descriptionEncripted", saved.getDescription());
    }

    @Test
    void testCreateNoteException() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID user_id = UUID.randomUUID();
        PriorityNames priority_name = PriorityNames.CRITICA;

        // When
        when(this.priorityRepository.getByName(any(PriorityNames.class))).thenReturn(Optional.of(new PriorityDBO(UUID.randomUUID(), PriorityNames.CRITICA)));
        when(this.noteRepository.save(any(NoteDBO.class))).thenReturn(NoteDataProvider.getNoteDBO());
        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());


        when(this.aesUtil.encrypt(anyString())).thenThrow(RuntimeException.class);

        // Then
        assertThrows(RuntimeException.class, () -> {
            this.noteAdapter.createNote(note, user_id, priority_name);
        });
    }

    @Test
    void testCreateNotePriorityNotFound(){
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID user_id = UUID.randomUUID();
        PriorityNames priority_name = PriorityNames.CRITICA;

        // When
        when(this.priorityRepository.getByName(any(PriorityNames.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.noteAdapter.createNote(note, user_id, priority_name);
        });

        assertEquals("PRIORITY NOT FOUND", exception.getMessage());
    }

    @Test
    void testUpdateNoteSuccessfully() throws Exception {
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID note_id = UUID.randomUUID();

        ArgumentCaptor<NoteDBO> noteUpdatedCaptor = ArgumentCaptor.forClass(NoteDBO.class);

        // When
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.of(NoteDataProvider.getNoteDBO()));
        when(this.aesUtil.encrypt(anyString())).thenReturn("newTitleEncrypted", "newDescriptionEncrypted");
        when(this.priorityRepository.getByName(any(PriorityNames.class))).thenReturn(Optional.of(PriorityDataProvider.getPriorityDBO()));

        when(this.noteRepository.save(any(NoteDBO.class))).thenReturn(NoteDataProvider.getNoteDBO());

        NoteModel result = this.noteAdapter.updateNote(note, note_id);

        // Then
        assertNotNull(result);

        verify(this.noteRepository).save(noteUpdatedCaptor.capture());
        NoteDBO updated = noteUpdatedCaptor.getValue();

        assertEquals("newTitleEncrypted", updated.getTitle());
        assertEquals("newDescriptionEncrypted", updated.getDescription());
    }

    @Test
    void testUpdateNoteNotFound(){
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID note_id = UUID.randomUUID();


        // When
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.noteAdapter.updateNote(note, note_id);
        });

        assertEquals("Note with id " + note_id + " not found", exception.getMessage());
    }

    @Test
    void testUpdateNoteAESException() throws Exception {
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID note_id = UUID.randomUUID();

        // When
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.of(NoteDataProvider.getNoteDBO()));
        when(this.aesUtil.encrypt(anyString())).thenThrow(RuntimeException.class);

        // Then
        assertThrows(RuntimeException.class, () -> {
            this.noteAdapter.updateNote(note, note_id);
        });
    }

    @Test
    void testUpdateNotePriorityNotFound() throws Exception {
        // Given
        NoteModel note = NoteDataProvider.getNoteModel();
        UUID note_id = UUID.randomUUID();

        // When
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.of(NoteDataProvider.getNoteDBO()));
        when(this.aesUtil.encrypt(anyString())).thenReturn("newTitleEncrypted", "newDescriptionEncrypted");
        when(this.priorityRepository.getByName(any(PriorityNames.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.noteAdapter.updateNote(note, note_id);
        });

        assertEquals("Priority not found", exception.getMessage());
    }

    @Test
    void testDeleteNoteSuccessfully(){
        // given
        UUID id = UUID.randomUUID();

        // when
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.of(NoteDataProvider.getNoteDBO()));
        doNothing().when(this.noteRepository).delete(any(NoteDBO.class));

        String result = this.noteAdapter.deleteNote(id);

        // THen
        assertNotNull(result);

        assertEquals("Note deleted successfully", result);
    }

    @Test
    void testDeleteNoteNotFound(){
        // given
        UUID id = UUID.randomUUID();

        // when
        when(this.noteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // THen
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.noteAdapter.deleteNote(id);
        });

        assertEquals("Note with id " + id + " not found", exception.getMessage());
    }
}
