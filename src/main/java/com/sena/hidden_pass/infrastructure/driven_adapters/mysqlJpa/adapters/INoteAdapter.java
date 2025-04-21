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
import com.sena.hidden_pass.infrastructure.mappers.NoteMapper;
import com.sena.hidden_pass.infrastructure.mappers.PriorityMapper;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import com.sena.hidden_pass.infrastructure.utils.AESUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class INoteAdapter implements NoteUseCases {

    private INoteRepository noteRepository;
    private IUserAdapter userAdapter;
    private IUserRepository userRepository;

    @Autowired
    private IPriorityRepository priorityRepository;

    @Override
    public Set<NoteModel> getAllNotesByUser(UUID user_id) {
        UserDBO userFounded = UserMapper.userModelToDBO(userAdapter.getUserById(user_id));
        Set<NoteModel> notes = userFounded.getNoteList().stream().map(NoteMapper::noteDBOToModel).collect(Collectors.toSet());

        return notes.stream().map(noteModel -> {
            try {
                noteModel.setTitle(AESUtil.decrypt(noteModel.getTitle()));
                noteModel.setDescription(AESUtil.decrypt(noteModel.getDescription()));

                return noteModel;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public NoteModel getNoteById(UUID id) {
        return NoteMapper.noteDBOToModel(noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Note with id " + id + " not found")));
    }

    @Override
    public NoteModel createNote(NoteModel note, UUID user_id, PriorityNames priority_name) {

       try{
           PriorityDBO priority =  priorityRepository.getByName(priority_name).orElseThrow(() -> new IllegalArgumentException("PRIORITY NOT FOUND"));

           note.setId_priority(PriorityMapper.priorityDBOToModel(priority));

           // AES
           note.setTitle(AESUtil.encrypt(note.getTitle()));
           note.setDescription(AESUtil.encrypt(note.getDescription()));

           NoteDBO noteSaved = noteRepository.save(NoteMapper.noteModelToDBO(note));
           UserDBO userFounded = UserMapper.userModelToDBO(userAdapter.getUserById(user_id));
           userFounded.getNoteList().add(noteSaved);

           userRepository.save(userFounded);

           return NoteMapper.noteDBOToModel(noteSaved);
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }
    }

    @Override
    public NoteModel updateNote(NoteModel note, UUID note_id) {

        NoteDBO noteDBO = NoteMapper.noteModelToDBO(getNoteById(note_id));

        try {
            noteDBO.setDescription(AESUtil.encrypt(note.getDescription()));
            noteDBO.setTitle(AESUtil.encrypt(note.getTitle()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        PriorityDBO priorityDBO = priorityRepository.getByName(note.getId_priority().getName()).orElseThrow(() -> new IllegalArgumentException("Priority not found"));
        noteDBO.setId_priority(priorityDBO);


        return NoteMapper.noteDBOToModel(noteRepository.save(noteDBO));
    }

    @Override
    public String deleteNote(UUID note_id) {

        NoteDBO noteDBO = NoteMapper.noteModelToDBO(getNoteById(note_id));
        noteRepository.delete(noteDBO);

        return "Note deleted successfully";
    }
}
