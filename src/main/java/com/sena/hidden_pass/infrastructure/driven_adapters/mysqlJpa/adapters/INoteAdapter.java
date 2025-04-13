package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.usecases.NoteUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.INoteRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class INoteAdapter implements NoteUseCases {

    private INoteRepository noteRepository;
    private IUserAdapter userAdapter;
    private IUserRepository userRepository;

    @Autowired
    private IPriorityRepository priorityRepository;



    //Find User By ID -> DBO
    private final Function<UUID, UserDBO> functionFindUserById = (user_id) -> userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("User not found"));

    //Find Note By ID -> DBO
    private final Function<UUID, NoteDBO> functionFindNoteById = (note_id) -> noteRepository.findById(note_id).orElseThrow(() -> new IllegalArgumentException("Note with id " + note_id + " not found"));

    // Find Priority By ID -> DBO
    private final Function<PriorityNames, PriorityDBO> functionFindPriorityByName = (name) -> priorityRepository.getByName(name).orElseThrow(() -> new IllegalArgumentException("Priority not found"));

    private final Function<NoteDBO, NoteDBO> functionSaveNoteRepository = (dbo) -> noteRepository.save(dbo);

    //Set priority to Note - Model
    private final BiFunction<PriorityDBO, NoteModel, NoteDBO> functionSetPriorityToNote = (priority, note) -> {
        //note.setId_priority(PriorityMapper.priorityDBOToModel(priority));
        NoteModel newNote = new NoteModel(
                note.getId_note(),
                note.getTitle(),
                note.getDescription(),
                PriorityMapperFunction.functionDBOToModel.apply(priority)
        );

        return NoteMapperFunction.functionModelToDBO.apply(newNote);
    };

    private final BiFunction<UserDBO, NoteDBO, UserDBO> functionAddNewNoteToUser = (user, note) -> {

        Set<NoteDBO> notesUpdate = Stream.concat(
                user.getNoteList().stream(),
                Stream.of(note)
        ).collect(Collectors.toSet());

        return new UserDBO(
                user.getId_usuario(),
                user.getUsername(),
                user.getEmail(),
                user.getMaster_password(),
                user.getUrl_image(),

                notesUpdate,
                Optional.ofNullable(user.getPasswordList()).orElse(new HashSet<>()),
                Optional.ofNullable(user.getFolderList()).orElse(new HashSet<>()),
                user.getSecurityCodes()
        );
    };

    private final Function<UserDBO, UserDBO> functionSaveUserRepository = (user) -> userRepository.save(user);


    @Override
    public Set<NoteModel> getAllNotesByUser(UUID user_id) {
        UserDBO userFounded = functionFindUserById.apply(user_id);
        return userFounded.getNoteList().stream()
                .map(NoteMapperFunction.functionDBOToModel)
                .collect(Collectors.toSet());
    }

    @Override
    public NoteModel getNoteById(UUID id) {
        return NoteMapperFunction.functionDBOToModel.apply(functionFindNoteById.apply(id));
    }

    @Override
    public NoteModel createNote(NoteModel note, UUID user_id, PriorityNames priority_name) {
        return Optional.ofNullable(priority_name)
                .map(functionFindPriorityByName)
                .map(priorityDBO -> functionSetPriorityToNote.apply(priorityDBO, note))
                .map(functionSaveNoteRepository)
                .map(noteDBO -> {
                    UserDBO userFounded = functionFindUserById.apply(user_id);
                    UserDBO newUser = functionAddNewNoteToUser.apply(userFounded, noteDBO);
                    functionSaveUserRepository.apply(newUser);
                    return noteDBO;
                })
                .map(NoteMapperFunction.functionDBOToModel)
                .orElseThrow(() -> new RuntimeException("Error creating note"));
    }

    @Override
    public NoteModel updateNote(NoteModel note, UUID note_id) {

        return Optional.ofNullable(note_id)
                .map(functionFindNoteById)
                .map(noteDBO -> {
                    PriorityDBO priorityDBO = functionFindPriorityByName.apply(note.getId_priority().getName());

                    NoteDBO newNote = new NoteDBO(
                            noteDBO.getId_note(),
                            priorityDBO,
                            note.getTitle(),
                            note.getDescription()
                    );

                    functionSaveNoteRepository.apply(newNote);

                    return newNote;
                })
                .map(NoteMapperFunction.functionDBOToModel)
                .orElseThrow(() -> new RuntimeException("Error updating note"));
    };

    @Override
    public String deleteNote(UUID note_id) {
        NoteDBO noteDBO = NoteMapper.noteModelToDBO(getNoteById(note_id));
        noteRepository.delete(noteDBO);

        return "Note deleted successfully";
    }
}
